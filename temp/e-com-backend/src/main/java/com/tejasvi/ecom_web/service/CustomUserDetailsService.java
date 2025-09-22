package com.tejasvi.ecom_web.service;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejasvi.ecom_web.model.LoginCredentials;
import com.tejasvi.ecom_web.model.Role;
import com.tejasvi.ecom_web.model.User;
import com.tejasvi.ecom_web.model.UserDetailsPrincipal;
import com.tejasvi.ecom_web.repository.UserLoginRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	
	UserLoginRepository userLoginRepository;
	
	public CustomUserDetailsService(UserLoginRepository userLoginRepository) {
		this.userLoginRepository = userLoginRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginCredentials credentials = userLoginRepository.findByEmail(username);
		
		
		
		Set<Role> role = credentials.getUser().getRole();
		
		
		return new UserDetailsPrincipal(credentials, role);
	}

}
