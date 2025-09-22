package com.tejasvi.ecom_web.service;



import java.util.Date;


import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tejasvi.ecom_web.dto.LoginRequest;
import com.tejasvi.ecom_web.dto.UserRegisterRequest;
import com.tejasvi.ecom_web.model.LoginCredentials;
import com.tejasvi.ecom_web.model.Role;
import com.tejasvi.ecom_web.model.User;
import com.tejasvi.ecom_web.repository.UserLoginRepository;
import com.tejasvi.ecom_web.repository.UserRegisterRepository;



@Service
public class UserService {
	
	private final UserLoginRepository userLoginRepository;
	private final UserRegisterRepository userRegisterRepository;
	private final CartService cartService;
	private AuthenticationManager authManager;
	private final JWTService jWTService;
	
	
	public UserService(UserLoginRepository userLoginRepository,
			UserRegisterRepository userRegisterRepository, CartService cartService, AuthenticationManager authManager, JWTService jWTService) {
		
		this.userLoginRepository = userLoginRepository;
		this.userRegisterRepository = userRegisterRepository;
		this.cartService = cartService;
		this.authManager = authManager;
		this.jWTService = jWTService;
	}
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	
	//Registration Service
	@Transactional
	public void addUser(UserRegisterRequest dto, Set<Role> role) {
		
		User user = new User();
		
		LoginCredentials loginCredential = new LoginCredentials();
		
		user.setFullName(dto.getFullName());
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setAddress(dto.getAddress());
		user.setCreatedAt(new Date());

		user.setRole(role);
		
		userRegisterRepository.save(user);
		cartService.creatCartForUser(user);
		
		loginCredential.setEmail(dto.getEmail());
		loginCredential.setPassword(encoder.encode(dto.getPassword()));
		loginCredential.setUser(user);
		
		userLoginRepository.save(loginCredential);
		
	}

	
	
	//Login Service
	public String verifyLogin(LoginRequest loginRequest) {
		
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		
		if(authentication.isAuthenticated()) {
			
			LoginCredentials loginCredentials = userLoginRepository.findByEmail(loginRequest.getEmail());
			System.out.println("Login Successfull");
			loginCredentials.setLastLoginAt(new Date());
			userLoginRepository.save(loginCredentials);
			System.out.println("Login success");
			return jWTService.getJWT(loginRequest.getEmail());
			
		}
		else {
			System.out.println("Login failed");
			return "Login failed";
		}
	}
}
