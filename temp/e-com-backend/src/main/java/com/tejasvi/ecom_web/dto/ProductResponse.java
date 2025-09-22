package com.tejasvi.ecom_web.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ProductResponse {
    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String description;
    private boolean productAvailable;
    private long stockQuantity;
    private Date releaseDate;
    private String categoryName;

    // Instead of raw image bytes, return a URL
    private String imageUrl;
}
