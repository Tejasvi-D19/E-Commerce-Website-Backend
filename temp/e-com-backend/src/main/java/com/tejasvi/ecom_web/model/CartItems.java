package com.tejasvi.ecom_web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(
	    uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"})
	)
public class CartItems {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Min(1)
	private long quantity;
	
	@ManyToOne
	@JoinColumn(name="product_id", unique=false)
	Product product;
	
	@ManyToOne
	@JoinColumn(name="cart_id", unique=false)
	@JsonIgnore
	Cart cart;
	
	
	@Override
    public int hashCode() {
        return Objects.hash(id); // ✅ only id
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CartItems other)) return false;
        return Objects.equals(this.id, other.id);
    }
}
