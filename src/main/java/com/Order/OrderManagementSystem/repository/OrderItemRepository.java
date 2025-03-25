package com.Order.OrderManagementSystem.repository;



import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Order.OrderManagementSystem.entity.OrderItem;
import com.Order.OrderManagementSystem.entity.User;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{
	
	List<OrderItem>findByCustomer(User user);
	List<OrderItem> findByDateBefore(LocalDateTime cutoffTime);



}
