package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Repositary.ProductTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ProductTransactionController {
    @Autowired
    private ProductTransactionRepository ptr;

    @RequestMapping(value="/getallproductsTransaction",method = GET)
    public String getAllProductsTransaction(Model model)
    {
        model.addAttribute("productsTransaction",ptr.findAll());
        return "producttransactionlist.html";
    }
}
