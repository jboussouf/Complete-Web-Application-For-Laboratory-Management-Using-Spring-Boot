package com.jboussouf.labmanagement.security;

import com.jboussouf.labmanagement.model.AcceptedUsers;
import com.jboussouf.labmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("trying to load "+username);
        AcceptedUsers appUser = accountService.loadAcceptedByUsername(username);
        if (appUser == null) throw new UsernameNotFoundException(String.format("User %s or password are incorrect", username));
        String[] roles =  appUser.getRoles().stream().map(u->u.getRoleName()).toArray(String[]::new);
        UserDetails userDetails = User
                .withUsername(appUser.getUsername()).password(appUser.getPassword()).roles(roles).build();
        return userDetails;
    }
}
