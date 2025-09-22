package com.tejasvi.ecom_web.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
	
	private String fullName;
	private long phoneNumber;
	private String address;
	private String email;
	private String password;
	
}
