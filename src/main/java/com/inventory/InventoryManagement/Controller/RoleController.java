package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.Customer;
import com.inventory.InventoryManagement.Models.Role;
import com.inventory.InventoryManagement.Repositary.RoleRepo;
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
public class RoleController {
@Autowired
private RoleRepo rr;

    @RequestMapping(value = "/addrole",method = {RequestMethod.GET,RequestMethod.POST})
    public String showroleform(@ModelAttribute("role") Role role, Model model) {
        model.addAttribute("role",new Role());
        return "addrole.html";
    }

    @PostMapping("/saverole")
    public String saveRole(@Valid @ModelAttribute("role") Role role, Errors errors)
    {
        if(errors.hasErrors()){
            log.error("Role Form Validation due to : "+errors.toString());
            return "addrole.html";
        }
        rr.save(role);
        return "redirect:/getallrole";
    }

    @RequestMapping(value="/getallrole",method = GET)
    public String getAllRole(Model model)
    {
        model.addAttribute("roles",rr.findAll());
        return "rolelist.html";
    }

    @GetMapping("/updaterole/{id}")
    public String getrolebyid(@PathVariable("id") int roleid, Model model)
    {
        Optional<Role> r=rr.findById(roleid);

        model.addAttribute("updrole", r.get());
        // model.addAttribute("updproduct",new ProductMaster());


        return "updaterole.html";
    }

    @RequestMapping("/updroles/{id}")
    public String updaetRole(@PathVariable("id") int roleid, Role r, BindingResult result , Model model)
    {
        if(result.hasErrors())
        {
            r.setRoleID(roleid);
            return "updaterole.html";
        }
        r.roleID=roleid;
        rr.save(r);
        model.addAttribute("roles",rr.findAll());
        return "redirect:/getallrole";
    }

    @GetMapping("/deleteRole/{id}")
    public String deleteRole(@PathVariable("id") int id, Model model) {
        Role role = rr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        role.roleID=id;
        rr.delete(role);
        return "redirect:/getallrole";
    }


}
