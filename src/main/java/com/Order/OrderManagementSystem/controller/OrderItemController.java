package com.Order.OrderManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Order.OrderManagementSystem.dtos.OrderItemDto;
import com.Order.OrderManagementSystem.entity.OrderItem;
import com.Order.OrderManagementSystem.service.OrderItemService;

@RestController
@RequestMapping("/user")
public class OrderItemController {
	
	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping("/createOrder")
	public ResponseEntity<?> createOrder(
		@RequestBody OrderItem orderItems,@AuthenticationPrincipal UserDetails userDetails) {
		

		return orderItemService.createOrder(orderItems, userDetails.getUsername());
	
		
	}
	
	@PutMapping("updateorder/{itemId}")
	public ResponseEntity<String> updateOrder( @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemId) {
		
		return orderItemService.updateOrder(userDetails.getUsername(), itemId);
	}
	
	@GetMapping("/getUserOrders")
	public ResponseEntity<?> getUserOrders( @AuthenticationPrincipal UserDetails userDetails ) {
		
		return orderItemService.getUserOrders(userDetails.getUsername());
	}

}
