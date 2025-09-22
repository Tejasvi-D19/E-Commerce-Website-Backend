package com.tejasvi.ecom_web.dto;

import java.math.BigDecimal;

import java.util.Date;

import lombok.Data;

@Data
public class AddProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private String description;
    private boolean productAvailable;
    private long stockQuantity;
    private Date releaseDate;
    private long categoryId; // send category by ID
}

