package com.jboussouf.labmanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
public class WebAppSecurity {
    @Autowired
    private UserDetailServiceImp userDetailsServiceImp;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests().requestMatchers("/public/**",
                "/", "/index", "/home","/createAccount", "public/**", "CSS/**",  "img/**").permitAll();
        http.formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/notExist").permitAll();
        http.authorizeHttpRequests().requestMatchers("/ADMIN/**", "ADMIN/**").hasRole("ADMIN");
        http.authorizeHttpRequests().requestMatchers("/STUDENT/**", "STUDENT/**").hasRole("STUDENT");
        http.authorizeHttpRequests().requestMatchers("DIRECTOR/**", "/DIRECTOR/**").hasRole("DIRECTOR");
        http.authorizeHttpRequests().requestMatchers("PROF/**", "/PROF/**").hasRole("PROF");
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/errorPage");
        http.userDetailsService(userDetailsServiceImp);
        return http.build();
    }
}
