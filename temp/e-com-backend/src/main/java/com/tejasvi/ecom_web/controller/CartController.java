package com.tejasvi.ecom_web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejasvi.ecom_web.model.Cart;
import com.tejasvi.ecom_web.model.LoginCredentials;
import com.tejasvi.ecom_web.model.User;
import com.tejasvi.ecom_web.repository.UserLoginRepository;
import com.tejasvi.ecom_web.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;
	private final UserLoginRepository userLoginRepository;

	public CartController(CartService cartService, UserLoginRepository userLoginRepository) {
		this.cartService = cartService;
		this.userLoginRepository = userLoginRepository;
	}
	
	
	@GetMapping
	public Cart getCart(Authentication authentication) {
		System.out.println("Hello I entered into get cart");
		User user = getCurrentUser(authentication);
		return cartService.getCart(user);
	}
	
	
	@PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
	@PostMapping("/items")
	public Cart addToCart(Authentication authentication, @RequestParam long productId,
			 @RequestParam long quantity) { //9980179671
		
		System.out.println("Entered into addToCart");
		User user = getCurrentUser(authentication);
		return cartService.addToCart(user, productId, quantity);
	}
	
	@PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
	@PutMapping("/items")
	public Cart updateItem(Authentication authentication, @RequestParam long productId,
			 @RequestParam long quantity) {
		User user = getCurrentUser(authentication);
		return cartService.updateItemQuantity(user, productId, quantity);
	}

	
	
	@DeleteMapping("/items/{productId}")
	public Cart removeItem(Authentication authentication, @PathVariable("productId") long productId) {
		User user = getCurrentUser(authentication);
		return cartService.removeItem(user, productId);
	}

	@DeleteMapping
	public Cart clearCart(Authentication authentication) {
		User user = getCurrentUser(authentication);
		return cartService.clearCart(user);
	}
	
	private User getCurrentUser(Authentication authentication) {
		System.out.println("Gettteing authenticatede user");
		String email = authentication.getName();
		LoginCredentials login = userLoginRepository.findByEmail(email);
		User user = login.getUser();
		return user;		
	}
}
