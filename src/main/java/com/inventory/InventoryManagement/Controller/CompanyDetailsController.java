package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.ImageUtil.ImagesUtil;
import com.inventory.InventoryManagement.Models.CompanyDetails;
import com.inventory.InventoryManagement.Models.Supplier;
import com.inventory.InventoryManagement.Models.User;
import com.inventory.InventoryManagement.Repositary.CompanyDetailsRepository;
import com.inventory.InventoryManagement.Repositary.UserRepo;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import com.inventory.InventoryManagement.Services.CompanyLogoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.Transient;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
public class CompanyDetailsController {

    @Autowired
    UserRepository userRepository;
    private final CompanyDetailsRepository companyRepository;

    public CompanyDetailsController(CompanyDetailsRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @RequestMapping(value = "/getcompany", method = {RequestMethod.GET})
    public String getcompany(HttpServletRequest request,Authentication authentication){
        User u=userRepository.finddByUserName(authentication.getName());
        int ID = u.getUserID();
        return "redirect:/getcompanybyuid/"+ID;
    }
    @RequestMapping(value = "/addCompDetails", method = {RequestMethod.GET,RequestMethod.POST})
    public String addCompDetails(@ModelAttribute("company")CompanyDetails company,Model model){
        model.addAttribute("company",new CompanyDetails());
        return "addCompanyDetails.html";
    }


    @Transient
    @PostMapping("/add")
    public String addCompany(@Valid @ModelAttribute("company") CompanyDetails company,Errors errors, HttpServletRequest request, @RequestParam("logo") MultipartFile logo) throws IOException {
        if(errors.hasErrors()){
            log.error("Company Form Validation due to : " + errors.toString());
            return "addCompanyDetails.html";
        }
        int ID = Integer.parseInt(request.getSession().getAttribute("uid").toString());
        Optional<User> u= userRepository.findById(ID);
        company.setUsers(u.get());
        company.setLogoFile(logo);
        companyRepository.save(company);
            return "redirect:/login?register=true";
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/upload",method = {RequestMethod.POST})
    public String uploadImage(@RequestParam("companyLogo")MultipartFile file, Errors errors, Model model, RedirectAttributes redirAttrs, @Valid @ModelAttribute("company") CompanyDetails companyDetails) throws IOException{
        if (errors.hasErrors()) {
            redirAttrs.addFlashAttribute("error", errors.toString());
            return "addCompanyDetails.html";
        }
        model.addAttribute("company",new CompanyDetails());
        companyDetails.setLogoName(file.getOriginalFilename());
        companyDetails.setLogoType(file.getContentType());
        companyDetails.setCompanyLogo(ImagesUtil.compressImage(file.getBytes()));
        redirAttrs.addFlashAttribute("success", "Supplier Added Successfully");
        companyRepository.save(companyDetails);
      return "dashbord.html";
    }

    @GetMapping("/getcompanybyuid/{id}")
    public String getCompanyByuserId(@PathVariable("id") int userid, Model model) {
        Optional<User> u=userRepository.findById(userid);
        CompanyDetails companydetails=companyRepository.findByusers(u.get());
        int companyid=companydetails.getCompanyID();
        Optional<CompanyDetails> cmp = companyRepository.findById(companyid);
        model.addAttribute("getComp", cmp.get());
        return "updateCompanyDetails.html";
    }

    @RequestMapping("/updateComp/{id}")
    public String updateCompany(@PathVariable("id") int companyid,@RequestParam("logo") MultipartFile logo,Authentication authentication, @Valid @ModelAttribute("getComp") CompanyDetails details, Errors errors, BindingResult result, Model model, RedirectAttributes redirAttrs) throws IOException {

        details.companyID = companyid;
        User u=userRepository.finddByUserName(authentication.getName());
        details.setUsers(u);
        details.setLogoFile(logo);
        if (errors.hasErrors()) {
            redirAttrs.addFlashAttribute("error", errors.toString());
            return "updateCompanyDetails.html";
        } else if (result.hasErrors()) {
            details.setCompanyID(companyid);
            return "updateCompanyDetails.html";
        }

        companyRepository.save(details);
        model.addAttribute("getComp", companyRepository.findById(companyid));
        redirAttrs.addFlashAttribute("success", "Company details Updated Successfully");
        return "redirect:/getcompanybyuid/"+u.getUserID();
    }
}
