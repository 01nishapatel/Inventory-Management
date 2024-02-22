package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.Product;
import com.inventory.InventoryManagement.Models.ProductTransaction;
import com.inventory.InventoryManagement.Models.Supplier;
import com.inventory.InventoryManagement.Models.Tax;
import com.inventory.InventoryManagement.Repositary.CategoryRepo;
import com.inventory.InventoryManagement.Repositary.TaxRepo;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
public class TaxController {
    @Autowired
    private TaxRepo taxRepo;

    @Autowired
    private CategoryRepo cr;

    private void loadProductAttributeValues(Model model) {
        model.addAttribute("categories", cr.findAll());
    }

    @RequestMapping(value = "/addTax",method = {RequestMethod.GET})
    public String createTaxForm(@ModelAttribute("t") Tax t, Model model, BindingResult result) {
        model.addAttribute("t",new Tax());
        loadProductAttributeValues(model);
        return "addtax.html";
    }
    @PostMapping("/saveTax")
    public String saveTax(@Valid @ModelAttribute("t") Tax t, BindingResult bindingResult, Errors errors, Model model)
    {
        if (errors.hasErrors()) {
           // log.error("Product Form Validation due to : " + errors.toString());
            loadProductAttributeValues(model);
            return "addTax.html";
        }
        taxRepo.save(t);
        return "redirect:/getalltaxes";
    }

    @RequestMapping(value="/getalltaxes",method = GET)
    public String getalltaxes(Model model)
    {
        model.addAttribute("taxes",taxRepo.findAll());

        return "taxlist.html";
    }

    @GetMapping("/updatetax/{id}")
    public String gettaxbyid(@PathVariable("id") int taxid, Model model) {
        Optional<Tax> p = taxRepo.findById(taxid);

        model.addAttribute("updtax", p.get());
        // model.addAttribute("updproduct",new ProductMaster());
        model.addAttribute("listcate", cr.findAll());

        return "updatetax.html";
    }

    @RequestMapping("/updTax/{id}")
    public String updateTax(@PathVariable("id") int taxid, Tax t, BindingResult result, Model model) {
        if (result.hasErrors()) {
            t.setTaxID(taxid);
            return "updatetax.html";
        }
        t.setTaxID(taxid);
        taxRepo.save(t);
        model.addAttribute("taxes", taxRepo.findAll());
        return "redirect:/getalltaxes";
    }

//    @Transactional
//    @GetMapping("/deleteTax/{id}")
//    public String deleteTax(@PathVariable("id") int id, Model model) {
//        Optional<Tax> t=taxRepo.findById(id);
//        Tax tax=t.get();
//        taxRepo.delete(tax);
//        return "redirect:/getalltaxes";
//    }

}
