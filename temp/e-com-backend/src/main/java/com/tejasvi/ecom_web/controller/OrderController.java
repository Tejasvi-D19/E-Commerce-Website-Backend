package com.tejasvi.ecom_web.controller;


import java.util.HashMap;

import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



import com.stripe.model.checkout.Session;
import com.tejasvi.ecom_web.model.LoginCredentials;
import com.tejasvi.ecom_web.model.Order;
import com.tejasvi.ecom_web.model.User;
import com.tejasvi.ecom_web.repository.UserLoginRepository;
import com.tejasvi.ecom_web.service.OrderService;

@Controller
@RequestMapping("/api")
public class OrderController {
	
	
	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Autowired 
	private OrderService orderService;
	
	
	
	
	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('Customer','Admin')")
	public ResponseEntity<?> createOrder(Authentication authentication) {
		System.out.println("Createing the order for user");
        try {
            User user = getCurrentUser(authentication);
            Order order = orderService.createOrder(user);
            
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            //logger.error("Unexpected error creating order: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: Failed to create order");
        }
    }
	
	
	
    @PostMapping("/{orderId}/payment-intent")
    @PreAuthorize("hasAnyAuthority('Customer','Admin')")
    public ResponseEntity<?> createSessionCheckout( @PathVariable long orderId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            Optional<Order> orderOptional = orderService.getOrderById(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Order order = orderOptional.get();

            // Check if user owns the order
            if (!(order.getUser().getId() == user.getId())) {
                return ResponseEntity.badRequest()
                        .body("Error: You can only create payment  for your own orders");
            }

            Session session = orderService.createSessionCheckout(order);

            Map<String, Object> response = new HashMap<>();
            response.put("Payment URL", session.getUrl());
            response.put("session Id", session.getId());

         
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            //logger.error("Unexpected error creating payment intent: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: Failed to create payment intent");
        }
    }


    @PostMapping("/confirm-payment")
    @PreAuthorize("hasAnyAuthority('Customer','Admin')")

    public ResponseEntity<?> confirmPayment(@RequestParam String sessionId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            Order order = orderService.confirmPayment(sessionId);

            // Check if user owns the order
            if (!(order.getUser().getId() == user.getId())) {
                return ResponseEntity.badRequest().body("Error: You can only confirm payments for your own orders");
            }

           
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
        
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
    
            return ResponseEntity.internalServerError().body("Error: Failed to confirm payment");
        }
    }


    @GetMapping("/my-orders")
    @PreAuthorize("hasAnyAuthority('Customer','Admin')")
    public ResponseEntity<Page<Order>> getMyOrders(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders = orderService.getUserOrders(user, pageable);
          
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            
            return ResponseEntity.internalServerError().build();
        }
    }

 
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyAuthority('Customer','Admin')")

    public ResponseEntity<Order> getOrderById( @PathVariable Long orderId,
            Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            Optional<Order> orderOptional = orderService.getOrderById(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Order order = orderOptional.get();

            // Check if user owns the order or is admin
            if (!(order.getUser().getId() == (user.getId())) && !(user.getRole().stream()
                    .anyMatch(role -> role.getRole_name().equals("Admin")))) {
                return ResponseEntity.badRequest().build();
            }

         
            return ResponseEntity.ok(order);
        } catch (Exception e) {
          
            return ResponseEntity.internalServerError().build();
        }
    }

  
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Admin')")

    public ResponseEntity<Page<Order>> getAllOrders(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders = orderService.getAllOrders(pageable);
           
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
           
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status,
            Authentication authentication) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
           
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
           
            return ResponseEntity.internalServerError().body("Error: Failed to update order status");
        }
    }

	
	
	
	
	
	private User getCurrentUser(Authentication authentication) {
		System.out.println("Gettteing authenticatede user");
		String email = authentication.getName(); 
		LoginCredentials login = userLoginRepository.findByEmail(email);
		User user = login.getUser();
		return user;		
	}
}
