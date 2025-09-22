package com.tejasvi.ecom_web.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddToCartRequest {
	
	
	//private long productId;

    @Min(1)
    private long quantity;
    
    
}


