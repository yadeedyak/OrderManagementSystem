package com.Order.OrderManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Order.OrderManagementSystem.entity.Item;
import com.Order.OrderManagementSystem.service.ItemService;

@RestController
@RequestMapping("/seller")
public class ItemController {

	@Autowired
	private ItemService itemservice;
	
	@PostMapping("/createitem")
	public ResponseEntity<String> createItem(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Item item) {

		if(userDetails==null) {
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("null");
		}

		return itemservice.createItem(userDetails.getUsername(),item);
		
		
	}
	
	@GetMapping("/get")
	public ResponseEntity<String> finduserbyname(@AuthenticationPrincipal UserDetails userDetails) {
		
		return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUsername());
		
	}
	
	@PutMapping("/updateItem")
	public ResponseEntity<String> updateItem(@AuthenticationPrincipal UserDetails userDetails,@RequestBody Item item) {


		return itemservice.updateItem(userDetails.getUsername(),item);
		
		
	}
	
	@GetMapping("/getSellerItems")
	public ResponseEntity<List<Item>> getSellerItems(@AuthenticationPrincipal UserDetails userDetails) {
		
		return itemservice.ListOfSellerItems(userDetails.getUsername());
		
	}
	
	@DeleteMapping("/delete/{itemId}")
	public ResponseEntity<String> deleteSellerItems(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long itemId) {
		
		return itemservice.deleteSellerItems(userDetails.getUsername(),itemId);
	}
	
	
}
