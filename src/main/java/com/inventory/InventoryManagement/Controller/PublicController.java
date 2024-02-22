package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.Role;
import com.inventory.InventoryManagement.Models.User;
import com.inventory.InventoryManagement.Repositary.RoleRepo;
import com.inventory.InventoryManagement.Repositary.SubscriptionRepo;
import com.inventory.InventoryManagement.Repositories.RoleRepository;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import com.inventory.InventoryManagement.Services.UserService;
import com.mysql.cj.jdbc.ha.RandomBalanceStrategy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("public")
public class PublicController {

    private final UserService userService;

    @Autowired
    public PublicController(UserService userService1){
        this.userService=userService1;
    }

    @Autowired
    private RoleRepo rr;
    @Autowired
    private SubscriptionRepo sr;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    private void loadProductAttributeValues(Model model) {
        model.addAttribute("subscriptions", sr.findAll());
    }

    @RequestMapping(value = "/register",method = {RequestMethod.GET,RequestMethod.POST})
    public String displayRegisterPage(@RequestParam(value = "alreadyExist",required = false) String alreadyExist,
            Model model){
        loadProductAttributeValues(model);
        String errorMessage=null;
        if(alreadyExist!=null){
            errorMessage="Email already exist !! Try to register with other email !!";
        }
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("user",new User());
        return "register.html";
    }

    @RequestMapping(value = "/createUser",method = {RequestMethod.POST})
    public String createUser(@Valid @ModelAttribute("user") User user, Errors errors, HttpServletRequest request,Model model){
        loadProductAttributeValues(model);
        if(errors.hasErrors()){
            return "register.html";
        }
        Role roleName=roleRepository.findByRoleName("ADMIN");
        user.setRoleID(roleName);
            String checkReturn = userService.createNewUser(user);
            if (checkReturn.equals("true")) {
                request.getSession().setAttribute("uid",user.getUserID());
                return "redirect:/addCompDetails";
                //return "redirect:/login?register=true";
            }
            else if(checkReturn.equals("redirect:/register?alreadyExist=true")){
                return "redirect:/public/register?alreadyExist=true";
            }
            else {

                return "register.html";
            }
    }

    @RequestMapping(value="/forgotPass",method = {RequestMethod.GET,RequestMethod.POST})
    public String displayForgotPWD(@RequestParam(value="NotExist",required = false) String NotExist,
                                    @RequestParam(value = "NoMatch",required = false) String NoMatch,
                                   @RequestParam(value = "NotSatisfy",required = false) String NotSatisfy,
                                    @RequestParam(value = "success",required = false) String success,
            Model model){
        String errorMessage=null;
        if(NotExist!=null){
            errorMessage="Email Doesn't exist !! Please Login !!";
        }
        else if(NoMatch!=null){
            errorMessage="Password Do Not Match !!";
        }
        else if(NotSatisfy!=null){
            errorMessage="Please enter proper data !!";
        }
        else if(success!=null){
            errorMessage="Link is sent to your mail ! Please check !!";
        }
        model.addAttribute("user",new User());
        model.addAttribute("errorMessage",errorMessage);
        return "forgotPwdPage.html";
    }

    @RequestMapping(value = "/ChangePWD",method = {RequestMethod.POST})
    public String changePWD(@Valid @ModelAttribute("user") User user,Errors errors) throws MessagingException, UnsupportedEncodingException {
        String token= UUID.randomUUID().toString();
        if(user.getEmail().equals("")){
            return "redirect:/public/forgotPass?NotSatisfy=true";
        }
        User checkIfExist= userRepository.readByEmail(user.getEmail());
        if(checkIfExist.equals(null)){
            return "redirect:/public/forgotPass?NotExist=true";
        }
        else{
            userService.updateResetPasswordToken(token,user.getEmail());
            String link="http://localhost:8034/public/confirm-reset?token="+token;
            sendEmail(user.getEmail(),link);
            return "redirect:/public/forgotPass?success=true";
        }
    }

    public void sendEmail(String recipientEmail,String Link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom("68.mansi.it2019@gmail.com","IM Support");
        helper.setTo(recipientEmail);
        String subject="Here's the link to reset your password !!";
        String content="<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + Link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);
    }


    @RequestMapping(value = "/confirm-reset",method = {RequestMethod.GET,RequestMethod.POST})
    public String showResetPWDPage(@RequestParam(value = "token",required = false) String token,
                                   @RequestParam(value = "NoMatch",required = false) String NoMatch,
                                   @RequestParam(value = "NotSatisfy",required = false) String NotSatisfy,
                                   @RequestParam(value = "success",required = false) String success,
                                   @RequestParam(value = "InvalidToken",required = false) String InvalidToken,
                                   Model model,HttpServletRequest request){
        String errorMessage=null;
        model.addAttribute("token",token);
        if(token!=null){
            User user= userRepository.findByResetPasswordToken(token);
            if(user==null) {
                errorMessage = "Invalid Token";
            }
            else {
                request.getSession().setAttribute("token", token);
            }
        }
        else if(NoMatch!=null){
            errorMessage="Password Do Not Match !!";
        }
        else if(NotSatisfy!=null){
            errorMessage="Please enter proper data !!";
        }
        else if(success!=null){
            errorMessage="You are all set ! Now LogIn !!";
        }
        else if(InvalidToken!=null){
            errorMessage="Invalid Token !!";
        }
        model.addAttribute("user",new User());
        model.addAttribute("errorMessage",errorMessage);
        return "confirmReset.html";
    }

    @RequestMapping(value = "/confirmChangePassword",method = RequestMethod.POST)
    public String confirmReset(@Valid @ModelAttribute("user") User user, Errors errors, HttpServletRequest request){
        String token=null;
        User user1=new User();
        if(token==null) {
            token = request.getSession().getAttribute("token").toString();
        }
        if(token!=null) {
            user1 = userRepository.findByResetPasswordToken(token);
        }
        if(user.getPassword().equals("")){
            return "redirect:/public/confirm-reset?NotSatisfy=true";
        }
        else if(user.getConfirmPassword().equals(""))
        {
            return "redirect:/public/confirm-reset?NotSatisfy=true";
        }
        else if(!user.getPassword().equals(user.getConfirmPassword()))
        {
            return "redirect:/public/confirm-reset?NoMatch=true";
        }
        else if(user1==null && token!=null){
            return "redirect:/public/confirm-reset?InvalidToken=true";
        }
        else{
            userService.UpdatePassword(user1,user.getConfirmPassword());
            request.removeAttribute("token");
            return "redirect:/public/confirm-reset?success=true";
        }
    }

//    @RequestMapping(value = "/ChangePWD",method = {RequestMethod.POST})
//    public String changePWD(@Valid @ModelAttribute("user") User user,Errors errors){
//
//        if(user.getPassword().equals("")){
//            return "redirect:/public/forgotPass?NotSatisfy=true";
//        }
//        else if(user.getConfirmPassword().equals(""))
//        {
//            return "redirect:/public/forgotPass?NotSatisfy=true";
//        }
//        else if(user.getPassword().equals(""))
//        {
//            return "redirect:/public/forgotPass?NotSatisfy=true";
//        }
//        String mail=null;
////        User userr=userRepository.findByEmail(user.getEmail());
////        if(userr!=null){
////            mail=userr.getUserName();
////        }
////        user.setUserName(mail);
////        user.setRoleID(userr.getRoleID());
////        user.setSubscriptions(userr.getSubscriptions());
//        boolean checks=user.getPassword().equals(user.getConfirmPassword());
//        if(!checks) {
//            return "redirect:/public/forgotPass?NoMatch=true";
//        }
//        String checkIfExist= userService.changePWD(user);
//        if(checkIfExist.equals("NotExist")){
//            return "redirect:/public/forgotPass?NotExist=true";
//        }
//        else if(checkIfExist.equals("true")){
//            return "redirect:/login?success=true";
//        }
//        else{
//            return "forgotPwdPage.html";
//        }
//    }

//    @RequestMapping(value = "/OnlyMail",method = {RequestMethod.GET,RequestMethod.POST})
//    public String DisplayOnlyMailPage(Model model){
//        model.addAttribute("user",new User());
//        return "confirmReset.html";
//    }
}
