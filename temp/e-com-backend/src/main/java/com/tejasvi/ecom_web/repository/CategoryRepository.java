package com.tejasvi.ecom_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejasvi.ecom_web.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
