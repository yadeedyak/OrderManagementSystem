package com.Order.OrderManagementSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.Order.OrderManagementSystem.entity.User;
import com.Order.OrderManagementSystem.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/update")
	public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserDetails userDetails,@RequestBody User user){
		
		
		return userService.updateUser(userDetails.getUsername(),user);
		
		
	}
	
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
		
		return userService.deleteUser(userDetails.getUsername());
		
	}

	@GetMapping("/getOrders")
	public ResponseEntity<?> getCustomerOrders(@AuthenticationPrincipal UserDetails userDetails) {
		return userService.getCustomerOrders(userDetails.getUsername());
	}

	
	

}
