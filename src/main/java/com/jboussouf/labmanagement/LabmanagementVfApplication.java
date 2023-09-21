package com.jboussouf.labmanagement;

import com.jboussouf.labmanagement.model.AppRole;
import com.jboussouf.labmanagement.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class LabmanagementVfApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabmanagementVfApplication.class, args);
	}
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner start(AccountService accountService){
		return args -> {
			List<AppRole> roles = accountService.loadAllRoles();
			if (roles.isEmpty()) {
				accountService.newRole(new AppRole("ADMIN"));
				accountService.newRole(new AppRole("STUDENT"));
				accountService.newRole(new AppRole("PROF"));
				accountService.newRole(new AppRole("DIRECTOR"));
			}
		};
	}

}
