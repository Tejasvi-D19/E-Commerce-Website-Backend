package com.tejasvi.ecom_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tejasvi.ecom_web.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByUserId(long id);

	@Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items ci LEFT JOIN FETCH ci.product WHERE c.user.id = :userId")
	Cart findByUserIdWithItems(@Param("userId") Long userId);

}
