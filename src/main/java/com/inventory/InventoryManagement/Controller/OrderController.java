package com.inventory.InventoryManagement.Controller;


import com.inventory.InventoryManagement.Models.Product;
import com.inventory.InventoryManagement.Models.Tax;
import com.inventory.InventoryManagement.Repositary.TaxRepo;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class OrderController {

    @GetMapping("/datatble")
    public String DisplayHomePage(){
        return "datatable.html";
    }



}
