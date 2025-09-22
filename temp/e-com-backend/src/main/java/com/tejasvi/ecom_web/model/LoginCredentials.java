package com.tejasvi.ecom_web.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringExclude;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class LoginCredentials {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(unique=true)
	private String email;
	private String password;
	private Date lastLoginAt;
	
	@OneToOne
	@ToString.Exclude
	@JoinColumn(name="user_id", nullable=false)
	//@JsonBackReference
	private User user;
	
	
}
