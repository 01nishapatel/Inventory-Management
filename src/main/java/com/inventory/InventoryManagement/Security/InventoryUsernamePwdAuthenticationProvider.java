package com.inventory.InventoryManagement.Security;

import com.inventory.InventoryManagement.Models.Role;
import com.inventory.InventoryManagement.Models.User;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email=authentication.getName();
        String pwd=authentication.getCredentials().toString();
        User user= userRepository.readByEmail(email);
        if(null!=user && user.getUserID()>0 && passwordEncoder.matches(pwd,user.getPassword())){
            return new UsernamePasswordAuthenticationToken(user.getUserName(),null,getGrantedAuthorities(user.getRoleID()));
        }
        else{
            throw new BadCredentialsException("Invalid Credentials !!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Role role){
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
