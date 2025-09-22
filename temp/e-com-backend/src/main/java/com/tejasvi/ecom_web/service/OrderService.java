package com.tejasvi.ecom_web.service;

import com.tejasvi.ecom_web.dto.OrderDto;

import com.tejasvi.ecom_web.model.*;
import com.tejasvi.ecom_web.repository.OrderRepository;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private StripeUtil stripeUtil;

    public Order createOrder(User user) {
        // Get user's cart
    	System.out.println("Inside create order service");
        Cart cart = cartService.getCart(user);
        //Set<CartItems> items = cart.getItems();
        
        Optional<Long> id = cart.getItems().stream().map(items -> items.getId()).findFirst();
        
        System.out.println(id.toString());
        
        System.out.println();

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total amount
        BigDecimal totalAmount = cartService.calculateCartTotal(cart);
        System.out.println("Total cart amount " + totalAmount);

        // Create order
        Order order = new Order(user, totalAmount, Order.OrderStatus.PENDING, Order.PaymentStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        
        System.out.println(savedOrder.toString());
        
        // Create order items from cart items
        for (CartItems cartItem : cart.getItems()) {
        	
            OrderItem orderItem = new OrderItem(
                    savedOrder,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice());
            System.out.println("Creating the orderItem");
            
            savedOrder.getOrderItems().add(orderItem);
        }
        
        // Clear the cart
        cartService.clearCart(user);

        
        
        System.out.println("Saving the oreder");
        return orderRepository.save(savedOrder);
    }

    
    public Session createSessionCheckout(Order order) throws StripeException {
    	
        String description = "Order #" + order.getId() + " - " + order.getUser().getLoginCredential().getEmail();
        Session session = stripeUtil.createSessionCheckout(order.getTotalAmount(), "usd", description);

        // Update order with payment intent ID
        order.setStripeSessionCheckoutId(session.getId());
        orderRepository.save(order);

        //logger.info("Created payment intent {} for order: {}", paymentIntent.getId(), order.getId());
        return session;
    }

   
    public Order confirmPayment(String sessionId) throws StripeException {
        // Get payment intent from Stripe
        //PaymentIntent paymentIntent = stripeUtil.getPaymentIntent(paymentIntentId);
        
        Session session = Session.retrieve(sessionId);

        // Find order by payment intent ID
        Order order = orderRepository.findByStripeSessionCheckoutId(sessionId);
                
        
        System.out.println(session.getPaymentStatus());
        System.out.println("Updating the order status");
        // Update order status based on payment status
        
        if ("paid".equals(session.getPaymentStatus())) {
        	
            order.setPaymentStatus(Order.PaymentStatus.COMPLETED);
            order.setStatus(Order.OrderStatus.PROCESSING);
            
            logger.info("Payment confirmed for order: {}", order.getId());
            
        } else {
            order.setPaymentStatus(Order.PaymentStatus.FAILED);
            logger.warn("Payment failed for order: {}", order.getId());
        }

        return orderRepository.save(order);
    }

   
    public Page<Order> getUserOrders(User user, Pageable pageable) {
        //logger.info("Fetching orders for user: {}", user.getEmail());
        return orderRepository.findByUserIdWithItems(user.getId(), pageable);
    }

 
    public Page<Order> getAllOrders(Pageable pageable) {
        logger.info("Fetching all orders");
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Optional<Order> getOrderById(Long orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId);
    }

    
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status);
        logger.info("Updated order {} status to: {}", orderId, status);
        return orderRepository.save(order);
    }

   
    public Order updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setPaymentStatus(paymentStatus);
        logger.info("Updated order {} payment status to: {}", orderId, paymentStatus);
        return orderRepository.save(order);
    }

    public OrderDto convertToDto(Order order) {
        Set<OrderDto.OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(item -> new OrderDto.OrderItemDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()))
                .collect(Collectors.toSet());

        return new OrderDto(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getFullName(),
                order.getUser().getLoginCredential().getEmail(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getStripeSessionCheckoutId(),
                orderItemDtos,
                order.getCreatedAt());
    }
}


