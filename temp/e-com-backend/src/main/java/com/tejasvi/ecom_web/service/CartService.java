package com.tejasvi.ecom_web.service;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.tejasvi.ecom_web.model.Cart;
import com.tejasvi.ecom_web.model.CartItems;
import com.tejasvi.ecom_web.model.Product;
import com.tejasvi.ecom_web.model.User;
import com.tejasvi.ecom_web.repository.CartItemsRepository;
import com.tejasvi.ecom_web.repository.CartRepository;
import com.tejasvi.ecom_web.repository.ProductRepository;

import jakarta.validation.constraints.Min;

@Service
public class CartService {
	
	
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final CartItemsRepository cartItemsRepository;
	
	public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemsRepository cartItemsRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.cartItemsRepository = cartItemsRepository;
	}

	public void creatCartForUser(User user) {
		Cart cart = new Cart();
		cart.setUser(user);	
		cartRepository.save(cart);
		
	}
	
	public Cart getCart(User user) {
			
		return cartRepository.findByUserIdWithItems(user.getId());
	}
			
	public Cart addToCart(User user, long productId, long quantity) {
		
		Cart cart = getCart(user);
		
		Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        
        Optional<CartItems> existingItem =
                cartItemsRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (existingItem.isPresent()) {
            
            CartItems item = existingItem.get();
            
            System.out.println("Before updating the quantity "+ item.getQuantity());
            
            item.setQuantity(item.getQuantity() + quantity);
            
            System.out.println("total quantity "+item.getQuantity() + quantity);
            
            System.out.println(" After updating the quantity "+ item.getQuantity());
            
            cartItemsRepository.save(item);
          
        } else {
            CartItems newItem = new CartItems();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemsRepository.save(newItem);
            
        }
        
        return cart;
	
	}


	public Cart removeItem(User user, long productId) {
		Cart cart = user.getCart();
		boolean remove = cart.getItems().removeIf(item -> item.getProduct().getId() == productId);
		
		if(remove) {
			System.out.println("Product removed form cart");
			return cartRepository.save(cart);
		}else {
			throw new RuntimeException("Product not found in cart");
		}
		
	}

	
	
	public Cart clearCart(User user) {
		Cart cart = user.getCart();
		 cart.getItems().clear();
		 return cartRepository.save(cart);
	}
	
	

	public Cart updateItemQuantity(User user, long productId, @Min(0) long quantity) {
		Cart cart = user.getCart();
		
		CartItems cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId() == productId).findFirst().orElseThrow(() -> new RuntimeException("Product not found in cart"));
		
		
		if(cartItem.getProduct().getStockQuantity() < quantity) {
			throw new RuntimeException("Insufficient stock available " + cartItem.getProduct().getStockQuantity());
		}
		
		cartItem.setQuantity(cartItem.getQuantity() + quantity);
		
		return cartRepository.save(cart);
	}

	 public java.math.BigDecimal calculateCartTotal(Cart cart) {
	        return cart.getItems().stream()
	                .map(item -> item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
	                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
	}
	
}
