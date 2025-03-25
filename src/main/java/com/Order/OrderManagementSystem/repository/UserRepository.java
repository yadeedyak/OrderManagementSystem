package com.Order.OrderManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Order.OrderManagementSystem.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

public Optional<User> findByUsername(String username);


}
