package com.Order.OrderManagementSystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Order.OrderManagementSystem.entity.User;
import com.Order.OrderManagementSystem.service.UserDetailsServiceImpl;
import com.Order.OrderManagementSystem.service.UserService;
import com.Order.OrderManagementSystem.utilty.JwtUtils;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JwtUtils jwtUtil;
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> createUser(@RequestBody User User) {
		
		return userService.createUser(User);
		
	}
	
	@PostMapping("/seller/signup")
	public ResponseEntity<String> createSeller(@RequestBody User user) {
		
		return userService.createSeller(user);
		
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
			UserDetails userDetails =userDetailsServiceImpl.loadUserByUsername(user.getUsername());
			String jwt=jwtUtil.generateToken(userDetails.getUsername());
			return new ResponseEntity<>(jwt,HttpStatus.OK);
			
		} catch(Exception e) {
			
			log.error("Excepetion occur while createAuthenticationToken",e);
			
			return new ResponseEntity<String>("Incorret username or password", HttpStatus.BAD_REQUEST);
			
		}
		
		
	}
	
	 
	
	
}

