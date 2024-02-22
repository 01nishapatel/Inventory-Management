package com.inventory.InventoryManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{

//        http.csrf().disable().authorizeHttpRequests().requestMatchers("/resources/**").permitAll()
//                .requestMatchers("/dashboard").permitAll()
//                .requestMatchers("/contact").permitAll().requestMatchers("/public/**").permitAll()
//                .and().formLogin().loginPage("/login").permitAll()
//                .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll()
//                .and().logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll()
//                .and().httpBasic();


        http
                .csrf().disable().authorizeRequests()
                .requestMatchers("/contact").authenticated()
                .requestMatchers("/dashBoard").authenticated()
                .requestMatchers("/public/register").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/getAllUsers").hasAnyRole("SUPERADMIN","ADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll()
                                .defaultSuccessUrl("/dashBoard").failureUrl("/login?error=true").permitAll()
                .and().logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).permitAll()
                .and().httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public JavaMailSender javaMailSender(){
//        return new JavaMailSenderImpl();
//    }
}
