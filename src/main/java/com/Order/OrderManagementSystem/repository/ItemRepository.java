package com.Order.OrderManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Order.OrderManagementSystem.entity.Item;
import com.Order.OrderManagementSystem.entity.User;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
	
	Item findByName(String name);
	List<Item> findBySeller(User user);

}
