package com.tejasvi.ecom_web.configuration;

import java.io.IOException;


import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejasvi.ecom_web.service.CustomUserDetailsService;
import com.tejasvi.ecom_web.service.JWTService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter{
	
	ApplicationContext context;
	JWTService jWTService;
	
	public JWTAuthFilter(JWTService jWTService, ApplicationContext context) {
		this.context = context;
		this.jWTService = jWTService;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		
		System.out.println("Authorization Header returned by the client to server = " + authHeader);
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			System.out.println("Token after separation of Bearer = " + token);
			
			userName = jWTService.extractUserName(token);
			System.out.println("User Name inside Token = " + userName);
		}
		
		System.out.println("Auth in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			System.out.println("Entered in to second if");
			
			UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(userName);
	
			System.out.println();
			System.out.println("User Details extracted using user name");
			System.out.println("Username: " + userDetails.getUsername());
			System.out.println("Password: " + userDetails.getPassword());
			System.out.println("Authorities: " + userDetails.getAuthorities());
			
			
			
			if(jWTService.validate(token, userDetails)) {
				
				System.out.println("Token is validated ");
				
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
				System.out.println("Auth in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
			}
		}
		
		filterChain.doFilter(request, response);
		
		System.out.println("Do next ");
					
					
					
					
				
			
	}

}
