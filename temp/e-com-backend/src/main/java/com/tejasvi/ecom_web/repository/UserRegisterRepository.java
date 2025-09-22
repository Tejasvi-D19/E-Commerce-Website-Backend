package com.tejasvi.ecom_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tejasvi.ecom_web.model.User;

@Repository
public interface UserRegisterRepository extends JpaRepository<User, Long> {

}
