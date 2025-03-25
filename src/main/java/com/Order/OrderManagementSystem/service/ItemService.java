package com.Order.OrderManagementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Order.OrderManagementSystem.entity.Item;
import com.Order.OrderManagementSystem.entity.User;
import com.Order.OrderManagementSystem.repository.ItemRepository;
import com.Order.OrderManagementSystem.repository.UserRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	public ResponseEntity<String> createItem(String username, Item item) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		item.setSeller(user);
		itemRepository.save(item);

		return ResponseEntity.status(HttpStatus.CREATED).body("Item was created");
	}

	public ResponseEntity<String> updateItem(String username, Item item) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		Item listedItem = itemRepository.findByName(item.getName());
		if (listedItem == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not found");
		}


		listedItem.setPrice(item.getPrice());
		listedItem.setSize(item.getSize());
		itemRepository.save(listedItem);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item was updated");
	}

	public ResponseEntity<List<Item>> ListOfSellerItems(String username) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		List<Item> items = itemRepository.findBySeller(user);
		return ResponseEntity.ok(items);
	}

	public ResponseEntity<String> deleteSellerItems(String username, Long itemId) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found");
		}

		Item item = itemRepository.findById(itemId).orElse(null);
		if (item == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item was not found");
		}

		if (!item.getSeller().getId().equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not authorized to delete this item");
		}

		itemRepository.delete(item);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item deleted successfully");
	}

	public Item getItem(Long itemId) {
		return itemRepository.findById(itemId).orElse(null);
	}
}
