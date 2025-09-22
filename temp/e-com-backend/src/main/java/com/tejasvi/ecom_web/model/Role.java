package com.tejasvi.ecom_web.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String role_name;
	
	@ManyToMany(mappedBy="role")
	@JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
	Set<User> user;
	
	
	
}
