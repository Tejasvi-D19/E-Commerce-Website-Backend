package com.tejasvi.ecom_web.dto;

import com.tejasvi.ecom_web.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Order Data Transfer Object
 * 
 * DTO for transferring order data between client and server
 * Contains order information and associated order items
 * 
 * @author Tejasvi
 * @version 1.0
 */
public class OrderDto {

    private long id;
    private long userId;
    private String userName;
    private String userEmail;
    private BigDecimal totalAmount;
    private Order.OrderStatus status;
    private Order.PaymentStatus paymentStatus;
    private String stripePaymentIntentId;
    private Set<OrderItemDto> orderItems;
    private LocalDateTime createdAt;

    // Default constructor required for JSON deserialization
    public OrderDto() {
    }

    // Constructor for creating order DTOs
    public OrderDto(Long id, Long userId, String userName, String userEmail,
            BigDecimal totalAmount, Order.OrderStatus status,
            Order.PaymentStatus paymentStatus, String stripePaymentIntentId,
            Set<OrderItemDto> orderItems, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.stripePaymentIntentId = stripePaymentIntentId;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }

    public Order.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Order.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public Set<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * OrderItem DTO for nested order item information
     */
    public static class OrderItemDto {
        private long id;
        private long productId;
        private String productName;
        private long quantity;
        private BigDecimal price;

        // Default constructor
        public OrderItemDto() {
        }

        // Constructor
        public OrderItemDto(Long id, Long productId, String productName, long quantity, BigDecimal price) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        // Getters and Setters
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}


