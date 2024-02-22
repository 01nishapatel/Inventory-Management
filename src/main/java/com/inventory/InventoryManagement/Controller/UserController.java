package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.CompanyDetails;
import com.inventory.InventoryManagement.Models.Role;
import com.inventory.InventoryManagement.Models.User;
import com.inventory.InventoryManagement.Repositary.RoleRepo;
import com.inventory.InventoryManagement.Repositary.SubscriptionRepo;
import com.inventory.InventoryManagement.Repositary.UserRepo;
import com.inventory.InventoryManagement.Repositories.RoleRepository;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import com.inventory.InventoryManagement.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    @Autowired
    private UserRepo ur;

    @Autowired
    private SubscriptionRepo sr;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleRepo rr;
    @Autowired
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepo subscriptionRepo;
    String  rname;
    public UserController(UserService userService1){
        this.userService=userService1;
    }

    private void loadProductAttributeValues(Model model) {

        model.addAttribute("roles", rr.findAll());
        model.addAttribute("subscriptions", sr.findAll());
    }

    @Transactional
    @RequestMapping(value="/getAllUsers",method = GET)
    public String getAllusers(Model model, HttpServletRequest request)
    {
//        rname= request.getSession().getAttribute("userrolename").toString();
        Role role= roleRepository.findByRoleName("ADMIN");
        List<User> u= role.getUsers();
//        model.addAttribute("users",role.getUsers());
        model.addAttribute("users",ur.findAll());
        return "userlist.html";
    }

    @RequestMapping(value="/getstaff",method = GET)
    public String getstaff(Model model,Authentication authentication)
    {
        User myuser=userRepository.finddByUserName(authentication.getName());
        int uid=myuser.getUserID();
        return "redirect:/ShowUserStaff/"+uid;
    }

    @Transactional
    @RequestMapping(value="/ShowUserStaff/{id}",method = GET)
    public String ShowUserStaff(@PathVariable("id") int userid,Model model, HttpServletRequest request)
    {
        User userdata=ur.findById(userid).orElseThrow(()->new IllegalArgumentException("Invalid User ID:"+userid));
        model.addAttribute("uname",userdata.getUserName());
//        rname= request.getSession().getAttribute("userrolename").toString();
        //Role role= roleRepository.findById(userdata.getRoleID().getRoleID()).orElseThrow(()->new IllegalArgumentException("Invalid Role ID:"+userdata.getRoleID().getRoleID()));
       // List<User> u= ur.findByparentUserID(userdata.getUserID());
        model.addAttribute("users",ur.findByparentUserID(userdata.getUserID()));
       // model.addAttribute("users",ur.findAll());
        return "allStaff.html";
    }


    @RequestMapping(value = "/addUser",method = {RequestMethod.GET,RequestMethod.POST})
    public String showuserform(Model model,
                               @RequestParam(value = "alreadyExist",required = false) String alreadyExist) {
        loadProductAttributeValues(model);
        String errorMessage=null;
        if(alreadyExist!=null){
            errorMessage="Email already exist !! Try to register with other email !!";
        }
        model.addAttribute("user",new User());
        model.addAttribute("errorMessage",errorMessage);
        return "addUser.html";
    }

    @RequestMapping(value = "/saveUser",method = {RequestMethod.POST})
    public String saveUser(@Valid @ModelAttribute("user") User user, Errors errors, Model model, Authentication authentication)
    {
        //model.addAttribute("user",new User());
        loadProductAttributeValues(model);
        if(errors.hasErrors()){
            return "addUser.html";
        }
        User user1=userRepository.finddByUserName(authentication.getName());
        user.setParentUserID(user1.getUserID());
        user.setSubscriptions(subscriptionRepo.findBySubscriptionName("Multi user"));
        String checkStatus=userService.createInternalUser(user);
        if(checkStatus.equals("true")){
            return "redirect:/getstaff";
        }
        else if(checkStatus.equals("redirect:/addUser?alreadyExist=true")){
            return "redirect:/addUser?alreadyExist=true";
        }
        else
            return "addUser.html";
    }
    @GetMapping("/updateuserdetail/{id}")
    public String getuserbyid(@RequestParam(value = "AlreadyExist",required = false) String AlreadyExist,
            @PathVariable("id") int userid, Model model)
    {
        Optional<User> u=ur.findById(userid);

        String errorMessage=null;
        if(AlreadyExist!=null){
            errorMessage="User with this mail already exist !!";
        }
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("upduser", u.get());
        //model.addAttribute("updproduct",new ProductMaster());
        model.addAttribute("listrole",rr.findAll());
        model.addAttribute("listsub",sr.findAll());

        return "updateuser.html";
    }

//    @GetMapping("/IsActiveUser/{id}")
//    public String IsActiveUser(@PathVariable("id") int userid,boolean IsActive, Model model)
//    {
//
//        model.addAttribute("upduser", u);
//        //model.addAttribute("updproduct",new ProductMaster());
//        model.addAttribute("listrole",rr.findAll());
//        model.addAttribute("listsub",sr.findAll());
//
//        return "updateuser.html";
//    }

    @GetMapping("/isactiveuser/{id}")
    public String IsActiveUser(@PathVariable("id") int userid,@RequestParam("isActive") boolean checkbox, User u, BindingResult result , Model model)
    {
        u.setIsActive(checkbox);
        //User u=ur.findById(userid).orElseThrow(()->new IllegalArgumentException("Invalid User ID:"+userid));
//        if(u.getIsActive()==true){
//            u.setIsActive(false);
//        }else {
//            u.setIsActive(true);
//        }
        u.UserID=userid;
        ur.save(u);
        model.addAttribute("users",ur.findAll());
        return "redirect:/getAllUsers";
    }

    @RequestMapping("/updusers/{id}")
    public String updaetUser(@PathVariable("id") int userid, User u, BindingResult result , Model model)
    {
        if(result.hasErrors())
        {
            u.setUserID(userid);
            return "updateuser.html";
        }
        u.UserID=userid;
        User user=userRepository.findByEmail(u.getEmail());
        u.setPassword(user.getPassword());
        u.setConfirmPassword(user.getConfirmPassword());
        u.setParentUserID(user.getParentUserID());
        u.setIsActive(user.getIsActive());
            ur.save(u);
        model.addAttribute("users",ur.findAll());
        return "redirect:/getAllUsers";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        User user = ur.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.UserID=id;
        ur.delete(user);
        return "redirect:/getAllUsers";
    }


}
