package com.tejasvi.ecom_web.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity

public class User {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private long id;
	
	private String fullName;
	private long phoneNumber;
	private String address;
	private Date createdAt;
	
	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private LoginCredentials loginCredential;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
	@JoinTable(
		    name = "user_role",
		    joinColumns = @JoinColumn(name = "user_id", nullable = false),
		    inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
		)
	Set<Role> role;
	
	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private Cart cart;
	
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 @JsonIgnore
	 private Set<Order> orders = new HashSet<>();
	
}
