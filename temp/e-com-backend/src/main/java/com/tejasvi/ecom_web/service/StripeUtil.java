package com.tejasvi.ecom_web.service;

import com.stripe.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import com.stripe.param.checkout.SessionCreateParams;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

/**
 * Stripe Utility Class
 * 
 * Handles Stripe payment processing operations
 * Provides methods for creating payment intents and processing payments
 * 
 * @author Tejasvi
 * @version 1.0
 */
@Component
public class StripeUtil {
	
	@Value("${stripe.secretKey}")
	private String secretKey;

	public Session createSessionCheckout(BigDecimal amount, String currency, String description) {
		
		Stripe.apiKey = secretKey;
		
		// Convert amount to cents (Stripe uses smallest currency unit)
		long totalAmount = amount.multiply(new BigDecimal("100")).longValue();
		
		SessionCreateParams.LineItem.PriceData.ProductData productData = 
				SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Order Payment")
                .build();
		SessionCreateParams.LineItem.PriceData priceData = 
				SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency(currency == null ? "USD" : currency)
				.setUnitAmount(totalAmount)
				.setProductData(productData)
				.build();
		
		SessionCreateParams.LineItem lineItem =  
				SessionCreateParams.LineItem.builder()
				.setQuantity((long) 1)
				.setPriceData(priceData)
				.build();
				
		SessionCreateParams param = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8080/success")
				.setCancelUrl("http://localhost:8080/cancle")
				.addLineItem(lineItem)
				.build();
		
		Session session = null;
		
		
			try {
				session = Session.create(param);
			} catch (StripeException e) {
				e.printStackTrace();
			}
		return session;
	}
	
}
