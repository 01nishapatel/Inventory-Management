package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.*;
import com.inventory.InventoryManagement.Repositary.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class ProductController {

    @Autowired
    private ProductRepo pr;

    @Autowired
    private CategoryRepo cr;

    @Autowired
    private SupplierRepository sr;

    @Autowired
    private ProductTransactionRepository ptr;

    @Autowired
    private JavaMailSender mailSender;

    private void loadProductAttributeValues(Model model) {
        model.addAttribute("categories", cr.findAll());
    }

    @RequestMapping(value = "/getallproducts", method = GET)
    public String getAllProducts(Model model) {
        model.addAttribute("products", pr.findAll());
        List<Product> pro = pr.findByproductQuantityLessThan(10);
        List<String> proStr = new ArrayList<String>();
        for (Product pr : pro) {
            proStr.add("The product " + pr.getProductName() + " has 10 or less than 10 quantity !!");
        }
        model.addAttribute("errormsg", proStr);
        return "productlist.html";
    }

    @RequestMapping(value = "/addproduct", method = {RequestMethod.GET})
    public String showCreateForm(@ModelAttribute("p") Product p, Model model, BindingResult result) {
        model.addAttribute("p", new Product());
        loadProductAttributeValues(model);
        return "addproduct.html";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute("p") Product p, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.error("Product Form Validation due to : " + errors.toString());
            loadProductAttributeValues(model);
            return "addproduct.html";
        }
        pr.save(p);
        return "redirect:/getallproducts";
    }

    @GetMapping("/updateproduct/{id}")
    public String getProductDataById(@PathVariable("id") int productid, Model model) {
        Optional<Product> p = pr.findById(productid);

        model.addAttribute("updproduct", p.get());
        // model.addAttribute("updproduct",new ProductMaster());
        model.addAttribute("listcate", cr.findAll());

        return "updateproduct.html";
    }

    @RequestMapping("/updProduct/{id}")
    public String updateProduct(@PathVariable("id") int productid, Product p, BindingResult result, Model model, HttpServletRequest request) {
        int ID = Integer.parseInt(request.getSession().getAttribute("supplierid").toString());
        if (result.hasErrors()) {
            p.setProductID(productid);
            return "updateproduct.html";
        }
        Supplier supplier = sr.findById(ID).orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id:" + ID));
        p.setSupplier(supplier);
        p.ProductID = productid;
        Product pro = pr.findById(productid).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productid));
        p.setProductQuantity(pro.getProductQuantity() + p.getProductQuantity());
        pr.save(p);
        model.addAttribute("products", pr.findAll());
        return "redirect:/getallProductBysupplier/" + ID;
    }

    @Transactional
    @GetMapping("/deletePro/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        int ID = Integer.parseInt(request.getSession().getAttribute("supplierid").toString());
        Product pro = pr.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<ProductTransaction> proTra = pro.getProductTransactions();
        ptr.deleteAll(proTra);
        pr.delete(pro);
        return "redirect:/getallProductBysupplier/" + ID;
    }

    @RequestMapping(value = "/supplierproductadd", method = {RequestMethod.GET})
    public String supplierproductaddform(@ModelAttribute("pro") Product p, Model model, BindingResult result) {
        model.addAttribute("pro", new Product());
        loadProductAttributeValues(model);
        return "supplierproductadd.html";
    }

    @PostMapping("/saveSupplierProduct")
    public String savesupplierProduct(@Valid @ModelAttribute("pro") Product p, BindingResult bindingResult, Errors errors, Model model, HttpServletRequest request, RedirectAttributes redirAttrs) {
        if (errors.hasErrors()) {
            log.error("Product Form Validation due to : " + errors.toString());
            loadProductAttributeValues(model);
            redirAttrs.addFlashAttribute("error", errors.toString());
            return "supplierproductadd.html";
        }
        int ID = Integer.parseInt(request.getSession().getAttribute("supplierid").toString());
        Supplier supplier = sr.findById(ID).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + ID));
        p.setSupplier(supplier);
        pr.save(p);

        Product prod = pr.findById(p.getProductID()).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + p.getProductID()));
        if (prod != null) {
            ProductTransaction proTra = new ProductTransaction();
            proTra.setProducts(prod);
            proTra.setSuppliers(supplier);
            proTra.setQuantity(prod.getProductQuantity());
            proTra.setIsAdded(true);
            proTra.setCustomerID(null);
            ptr.save(proTra);
        }
        redirAttrs.addFlashAttribute("success", "Product Updated Successfully");
        return "redirect:/getallProductBysupplier/" + ID;
    }

    @Transactional
    @RequestMapping(value = "/reorderQtyAdd/{id}", method = RequestMethod.GET)
    public String reorderQtyAdd(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        Product prod = pr.findById(id).orElseThrow(() -> new IllegalArgumentException("Invali ID" + id));
        request.getSession().setAttribute("productid", id);
        model.addAttribute("prodName", prod.getProductName());
        model.addAttribute("prodDisc", prod.getProductDescription());
        model.addAttribute("prodPrice", prod.getProductPrice());
        model.addAttribute("prodSup", prod.getSupplier().getSupplierName());
        model.addAttribute("p", new Product());
        return "reorderQty.html";
    }

    @RequestMapping(value = "/reorderproduct", method = {RequestMethod.POST, RequestMethod.GET})
    public String reorderproduct(@ModelAttribute("p") Product p, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        int ID = Integer.parseInt(request.getSession().getAttribute("productid").toString());
        Product prod = pr.findById(ID).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + ID));
        Supplier supl = prod.getSupplier();
        sendEmailToSupplier(supl.getSupplierEmail(), prod, p.getProductQuantity());
        return "redirect:/getallproducts";

    }

    public void sendEmailToSupplier(String recipientEmail, Product pro, int qty) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("68.mansi.it2019@gmail.com", "IM Support");
        helper.setTo(recipientEmail);
        String subject = "We want to Reorder this Product: " + pro.getProductName();
        String content = "<p>Hello,</p>"
                + "<p>As You are supplier of our Company.</p>"
                + "<p>We have low stock of " + pro.getProductName() + "</p>"
                + "<br>"
                + "<p>We want re-order the " + pro.getProductName() + " with " + qty + " Quanty .</p>"
                + "<br>"
                + "<p> Thank You</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
