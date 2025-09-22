package com.tejasvi.ecom_web.model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;




public class UserDetailsPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LoginCredentials loginCredentials;
	 
	private Set<Role> role;

	public UserDetailsPrincipal(LoginCredentials loginCredentials, Set<Role> role) {
		this.loginCredentials = loginCredentials;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return role.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRole_name())).collect(Collectors.toList());
		
		
	}

	@Override
	public String getPassword() {
		
		return loginCredentials.getPassword();
	}

	@Override
	public String getUsername() {
		
		return loginCredentials.getEmail();
	}

}
