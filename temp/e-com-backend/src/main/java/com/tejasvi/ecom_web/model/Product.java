package com.tejasvi.ecom_web.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;
    private String brand;
    private BigDecimal price;
    private String description;
    private boolean productAvailable;
    private long stockQuantity;
    private Date releaseDate;

    private String imageName;
    private String imageType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")  // for MySQL
    private byte[] imageData;
    
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
    
    @OneToMany(mappedBy="product")
    @JsonIgnore
    Set<CartItems> cartItems;
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product other)) return false;
        return Objects.equals(id, other.id);
    }
    

	


}
