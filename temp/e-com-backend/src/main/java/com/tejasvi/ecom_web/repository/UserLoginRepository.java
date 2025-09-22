package com.tejasvi.ecom_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejasvi.ecom_web.model.LoginCredentials;

@Repository
public interface UserLoginRepository extends JpaRepository<LoginCredentials, Long> {

	boolean existsByEmail(String email);

	LoginCredentials findByEmail(String username);

	

}
