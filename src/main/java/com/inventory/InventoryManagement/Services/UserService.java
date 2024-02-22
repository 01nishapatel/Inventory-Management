package com.inventory.InventoryManagement.Services;

import com.inventory.InventoryManagement.Models.Role;
import com.inventory.InventoryManagement.Models.Subscription;
import com.inventory.InventoryManagement.Models.User;
import com.inventory.InventoryManagement.Repositary.SubscriptionRepo;
import com.inventory.InventoryManagement.Repositories.RoleRepository;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createNewUser(User user){
        boolean isSaved=false;
        Role role=roleRepository.findRoleNameByRoleID(user.getRoleID().getRoleID());
        Role roleName=roleRepository.findByRoleName("ADMIN");
        Subscription subscription=subscriptionRepo.findSubscriptionNameBySubscriptionID(user.getSubscriptions().getSubscriptionID());
        user.setSubscriptions(subscription);
        user.setRoleID(roleName);
        user.setParentUserID(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(user.getPassword());
        String email=user.getEmail();
        User user1=userRepository.findByEmail(email);
        if(user1!=null){
            return "redirect:/register?alreadyExist=true";
        }
        else if(null!=user){
            user=userRepository.save(user);
            if(user.getUserID()>0){
                isSaved=true;
                return "true";
            }
            else{
                return "false";
            }
        }
        else return "false";
    }

    public String createInternalUser(User user){
        boolean isSaved=false;
        Subscription subscription=subscriptionRepo.findSubscriptionNameBySubscriptionID(user.getSubscriptions().getSubscriptionID());
        user.setSubscriptions(subscription);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(user.getPassword());
        String mailCheck=user.getEmail();
        User user1=userRepository.findByEmail(mailCheck);
        if(user1!=null){
            return "redirect:/addUser?alreadyExist=true";
        }
        else if(null!=user){
            user=userRepository.save(user);
            if(user.getUserID()>0){
                isSaved=true;
                return "true";
            }
            else return "false";
        }
        else return "false";
    }
    public String changePWD(User user){
        boolean isSaved=false;
        User checkUser=userRepository.readByEmail(user.getEmail());
        if(null==checkUser){
            return "NotExist";
        }
        else if (checkUser.getUserID()>0){
            checkUser.setPassword(passwordEncoder.encode(user.getPassword()));
            checkUser.setConfirmPassword(checkUser.getPassword());
            userRepository.save(checkUser);
            return "true";
        }
        else{
            return "false";
        }
    }

    public String updateResetPasswordToken(String token,String email){
        User user=userRepository.findByEmail(email);
        if(user!=null)
        {
            user.setResetPasswordToken(token);
            userRepository.save(user);
            return "true";
        }
        else{
            return "NotAUser";
        }
    }

    public void UpdatePassword(User user,String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setConfirmPassword(user.getPassword());
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public int CountAdminUser(){
        Role rolename= roleRepository.findByRoleName("ADMIN");
        List<User> users=userRepository.findByRoleID(rolename);
        int count=users.size();
        return count;
    }

    public User name(Authentication authentication){
        User user=userRepository.findByEmail(authentication.getName());
        return user;
    }
}
