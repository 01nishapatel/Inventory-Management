package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.Category;
import com.inventory.InventoryManagement.Models.Product;
import com.inventory.InventoryManagement.Repositary.CategoryRepo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryRepo cr;

    @RequestMapping(value = "/getallcategories", method = GET)
    public String getAllCategory(Model model) {
        model.addAttribute("categories", cr.findAll());
        return "categorylist.html";
    }

    @RequestMapping(value = "/addcategory",method = {RequestMethod.GET,RequestMethod.POST})
    public String showCategoryform(@ModelAttribute("c") Category c, Model model, BindingResult result) {
        model.addAttribute("c",new Category());
        return "addcategory.html";
    }

    @PostMapping("/saveCateory")
    public String saveCategory(@Valid @ModelAttribute("c") Category c, BindingResult bindingResult, Errors errors) {
        if (errors.hasErrors()) {
            log.error("Category Form Validation due to : " + errors.toString());
            return "addcategory.html";
        }
        cr.save(c);
        return "redirect:/getallcategories";
    }

    @GetMapping("/updatecategory/{id}")
    public String getCategoryByid(@PathVariable("id") int categoryid, Model model) {
        Optional<Category> c = cr.findById(categoryid);

        model.addAttribute("updcate", c.get());
        // model.addAttribute("updp",new ProductMaster());
        // model.addAttribute("listcate",cr.findAll());

        return "updatecategory.html";
    }

    @RequestMapping("/updCategory/{id}")
    public String updateCategory(@PathVariable("id") int categoryid, Category c, BindingResult result, Model model) {
        if (result.hasErrors()) {
            c.setCategoryID(categoryid);
            return "updatecategory.html";
        }
        c.CategoryID = categoryid;
        cr.save(c);
        model.addAttribute("categories", cr.findAll());
        return "redirect:/getallcategories";
    }

    @GetMapping("/deleteCate/{id}")
    public String deleteCatgeory(@PathVariable("id") int id, Model model) {
        Category cate = cr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        cate.CategoryID = id;
        cr.delete(cate);
        return "redirect:/getallcategories";
    }
}
