package com.tejasvi.ecom_web.configuration;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	UserDetailsService userDetailsService;

	JWTAuthFilter jWTAuthFilter;

	public SecurityConfig(UserDetailsService userDetailsService, JWTAuthFilter jWTAuthFilter) {

		this.userDetailsService = userDetailsService;
		this.jWTAuthFilter = jWTAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/swagger-ui.html").permitAll()
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

						.requestMatchers("/api/register/**").permitAll()
						.requestMatchers("/api/login/**").permitAll()
						.requestMatchers("/products/search/**").permitAll()

						// Protected role-based endpoints (after login)
						.requestMatchers("/api/products/**").hasAnyAuthority("Customer", "Admin")
						.requestMatchers("/api/cart/**").hasAnyAuthority("Customer", "Admin")
						.requestMatchers("/api/product/{id}/**").hasAnyAuthority("Customer", "Admin")
						.requestMatchers("/api/cart/items/{productId}").hasAnyAuthority("Customer", "Admin")
						.requestMatchers("/api/create").hasAnyAuthority("Customer", "Admin")

						// only Admin access
						.requestMatchers("/api/admin/product/**").hasAuthority("Admin")
						.requestMatchers("/api/admin/product/{id}/**").hasAuthority("Admin")
						.requestMatchers("/api/admin/product/{id}/**").hasAuthority("Admin")
						.requestMatchers("/api/admin/**").hasAuthority("Admin")
						// .requestMatchers("/api/admin/products/**").hasAuthority("Admin")

						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				//.httpBasic(b->b.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jWTAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
