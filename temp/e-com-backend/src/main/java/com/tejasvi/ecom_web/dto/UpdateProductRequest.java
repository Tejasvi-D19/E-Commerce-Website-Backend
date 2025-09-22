package com.tejasvi.ecom_web.dto;

import java.math.BigDecimal;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private String description;
    private Boolean productAvailable;
    private long stockQuantity;
    private Date releaseDate;
    private long categoryId; // optional
}
