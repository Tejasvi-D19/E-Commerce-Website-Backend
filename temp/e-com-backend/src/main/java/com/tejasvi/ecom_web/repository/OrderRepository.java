package com.tejasvi.ecom_web.repository;

import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.tejasvi.ecom_web.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	//Optional<Order> findByStripePaymentIntentId(String paymentIntentId);

	
	@Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user.id = :userId")
	Page<Order> findByUserIdWithItems(long userId, Pageable pageable);



	Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);


	Order findByStripeSessionCheckoutId(String sessionId);

}
