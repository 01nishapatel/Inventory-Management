package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.Product;
import com.inventory.InventoryManagement.Models.Supplier;
import com.inventory.InventoryManagement.Repositary.ProductRepo;
import com.inventory.InventoryManagement.Repositary.ProductTransactionRepository;
import com.inventory.InventoryManagement.Repositary.SupplierRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
public class SupplierController {
    @Autowired
    private SupplierRepository sr;

    @Autowired
    private ProductRepo pro;
    //all
    @RequestMapping(value = "/getallsupplier", method = GET)
    public String getAllSupplier(Model model) {
        model.addAttribute("suppliers", sr.findAll());
        // log.info(sr.findAll().toString());
        return "supplierlist.html";
    }

    //add page
    @RequestMapping(value = "/addsupplier", method = {RequestMethod.GET,RequestMethod.POST})
    public String showSupplierAddForm(@ModelAttribute("supplier") Supplier supplier, Model model) {
        model.addAttribute("supplier", new Supplier());
        return "addsupplier.html";
    }

    //add save
    @PostMapping("/savesupplier")
    public String saveSupplier(@Valid @ModelAttribute("supplier") Supplier supplier, Errors errors, RedirectAttributes redirAttrs) {
        if (errors.hasErrors()) {
            redirAttrs.addFlashAttribute("error", errors.toString());
            return "addsupplier.html";
        }
        sr.save(supplier);
        redirAttrs.addFlashAttribute("success", "Supplier Added Successfully");
        return "redirect:/getallsupplier";
    }

    //get by id
    @GetMapping("/getsupplierbyid/{id}")
    public String getSupplierById(@PathVariable("id") int supplierid, Model model) {
        Optional<Supplier> s = sr.findById(supplierid);
        model.addAttribute("getSup", s.get());
        return "updatesupplier.html";
    }

    //update supplier
    @RequestMapping("/updsupplier/{id}")
    public String updateSupplier(@PathVariable("id") int supplierid, @Valid @ModelAttribute("getSup") Supplier suppliers, Errors errors, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (errors.hasErrors()) {
            redirAttrs.addFlashAttribute("error", errors.toString());
            return "updatesupplier.html";
        } else if (result.hasErrors()) {
            suppliers.setSupplierID(supplierid);
            return "updatesupplier.html";
        }
        suppliers.SupplierID = supplierid;
        sr.save(suppliers);
        model.addAttribute("suppliers", sr.findAll());
        redirAttrs.addFlashAttribute("success", "Supplier Updated Successfully");
        return "redirect:/getallsupplier";
    }

    //delete
    @GetMapping("/deletesupplier/{id}")
    public String deleteSupplier(@PathVariable("id") int id, Model model) {
        Supplier supplier = sr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id:" + id));
        supplier.SupplierID = id;
        sr.delete(supplier);
        return "redirect:/getallsupplier";
    }

    //getProductBySupplierID
    @Transactional
    @RequestMapping(value = "/getallProductBysupplier/{id}", method = GET)
    public String getallProductBysupplier(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        request.getSession().setAttribute("supplierid",id);
        Supplier sup=sr.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid supplier ID:"+id));
        List<Product> productList=sup.getProducts();
        if(productList.stream().count()>0){
            model.addAttribute("products", sup.getProducts());
            List<String> proStr = new ArrayList<String>();
            for (Product pr : sup.getProducts()) {
                if(pr.getProductQuantity()<=10) {
                    proStr.add("The product " + pr.getProductName() + " has 10 or less than 10 quantity !!");
                }
            }
            model.addAttribute("errormsg", proStr);
        }
        // log.info(sr.findAll().toString());
        return "supplierproductlist.html";
    }

}
