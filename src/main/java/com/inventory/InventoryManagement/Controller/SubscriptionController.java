package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.Category;
import com.inventory.InventoryManagement.Models.Subscription;
import com.inventory.InventoryManagement.Repositary.SubscriptionRepo;
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
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@Controller
public class SubscriptionController {

    @Autowired
    private SubscriptionRepo sr;

    @RequestMapping(value="/getallsubscriptions",method = {GET})
    public String getAllSubsciptions(Model model)
    {
        model.addAttribute("subs",sr.findAll());
        return "sublist.html";
    }

    @RequestMapping(value = "/addSubsciption",method = {GET,RequestMethod.POST})
    public String showCategoryform(@ModelAttribute("s") Subscription s, Model model, BindingResult result) {
        model.addAttribute("s",new Subscription());
        return "addsub.html";
    }

    @PostMapping("/saveSubscription")
    public String saveSubs(@Valid @ModelAttribute("s") Subscription s, BindingResult bindingResult, Errors errors)
    {
        if (errors.hasErrors()) {
            log.error("Category Form Validation due to : " + errors.toString());
            return "addsub.html";
        }
        sr.save(s);
        return "redirect:/getallsubscriptions";
    }

    @GetMapping("/updateSubscription/{id}")
    public String getSubByid(@PathVariable("id") int subid, Model model)
    {
        Optional<Subscription> s=sr.findById(subid);

        model.addAttribute("updsub", s.get());
        // model.addAttribute("updp",new ProductMaster());
        // model.addAttribute("listcate",cr.findAll());

        return "updatesub.html";
    }

    @RequestMapping("/updsub/{id}")
    public String updaetsub(@PathVariable("id") int subid, Subscription s, BindingResult result , Model model)
    {
        if(result.hasErrors())
        {
            s.setSubscriptionID(subid);
            return "updatesub.html";
        }
        s.subscriptionID=subid;
        sr.save(s);
        model.addAttribute("subs",sr.findAll());
        return "redirect:/getallsubscriptions";
    }

    @GetMapping("/deletesub/{id}")
    public String deleteSub(@PathVariable("id") int id, Model model) {
        Subscription sub = sr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        sub.subscriptionID=id;
        sr.delete(sub);
        return "redirect:/getallsubscriptions";
    }
}
