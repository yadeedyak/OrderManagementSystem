package com.Order.OrderManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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


	@DeleteMapping("/deleteOrder/{id}")
	public ResponseEntity<?> cancelOrder(@PathVariable("id") Long orderItemId, @AuthenticationPrincipal UserDetails userDetails) {

		return orderItemService.deleteOrder(orderItemId,userDetails.getUsername());

	}


}
