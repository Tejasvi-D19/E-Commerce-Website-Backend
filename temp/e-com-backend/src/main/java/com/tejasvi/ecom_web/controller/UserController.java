package com.tejasvi.ecom_web.controller;

import java.util.HashSet;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejasvi.ecom_web.dto.LoginRequest;
import com.tejasvi.ecom_web.dto.UserRegisterRequest;
import com.tejasvi.ecom_web.model.Role;
import com.tejasvi.ecom_web.repository.UserLoginRepository;
import com.tejasvi.ecom_web.service.UserService;


@RestController
@RequestMapping("/api")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	private final UserService userService;
	
	private final UserLoginRepository loginRepository;
	
	public UserController(UserService userService, UserLoginRepository loginRepository) {
		this.loginRepository = loginRepository;
		this.userService = userService;
	}
	
	
	
	
	
	@PostMapping("/register/customer")
	public void addCustomer(@RequestBody UserRegisterRequest userRegisterRequest) {
		
		log.info("Registering Customer : {}", userRegisterRequest);
		
		if(loginRepository.existsByEmail(userRegisterRequest.getEmail())) {
			log.error("Registration failed: Email {} already exists", userRegisterRequest.getEmail());
			throw new RuntimeException("Error: Email is already Exist!");
		}
		Role role = new Role();
		role.setId(2);
		role.setRole_name("customer");
		Set<Role> roles = new HashSet<>();
		 	roles.add(role);
		userService.addUser(userRegisterRequest, roles);
	}
	
	
	
	
	
	@PostMapping("/register/admin")
	public void addUser(@RequestBody UserRegisterRequest userRegisterRequest) {
		
		
		log.info("Registering Admin : {}", userRegisterRequest);
		
		if(loginRepository.existsByEmail(userRegisterRequest.getEmail())) {
			log.error("Registration failed: Email {} already exists", userRegisterRequest.getEmail());
			throw new RuntimeException("Error: Email is already Exist!");
		}
		Role role = new Role();
		role.setId(1);
		role.setRole_name("Admin");
		Set<Role> roles = new HashSet<>();
		 	roles.add(role);
		userService.addUser(userRegisterRequest, roles);
	}
	
	
	
	
	
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest) {
		return userService.verifyLogin(loginRequest);
	}

}
