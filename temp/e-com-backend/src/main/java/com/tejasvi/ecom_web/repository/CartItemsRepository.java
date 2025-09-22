package com.tejasvi.ecom_web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejasvi.ecom_web.model.CartItems;


public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

	Optional<CartItems> findByCartIdAndProductId(long id, long productId);
	
}
