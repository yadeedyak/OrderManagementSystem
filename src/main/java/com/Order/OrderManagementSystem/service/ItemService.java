package com.Order.OrderManagementSystem.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final String ITEM_KEY_PREFIX = "ITEM:";

	public ResponseEntity<String> createItem(String username, Item item) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		// Save in MySQL
		item.setSeller(user);
		Item savedItem = itemRepository.save(item);

		// Save in Redis
		redisTemplate.opsForValue().set(ITEM_KEY_PREFIX + savedItem.getId(), savedItem);

		return ResponseEntity.status(HttpStatus.CREATED).body("Item was created and cached in Redis");
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

		// Update item details
		listedItem.setPrice(item.getPrice());
		listedItem.setSize(item.getSize());
		itemRepository.save(listedItem); // Update in MySQL

		// Update Redis manually
		String redisKey = "item:" + listedItem.getId(); // Key format: item:1
		redisTemplate.opsForValue().set(redisKey, listedItem);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item was updated and cached");
	}


//	public ResponseEntity<String> updateItem(String username, Item item) {
//		User user = userRepository.findByUsername(username).orElse(null);
//		if (user == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
//		}
//
//		Item listedItem = itemRepository.findByName(item.getName());
//		if (listedItem == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not found");
//		}
//
//
//		listedItem.setPrice(item.getPrice());
//		listedItem.setSize(item.getSize());
//		itemRepository.save(listedItem);
//
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item was updated");
//	}


	public ResponseEntity<List<Item>> listOfSellerItems(String username) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		String redisKey = "seller_items:" + user.getId();

		// Check Redis for cached items
		List<Item> cachedItems = (List<Item>) redisTemplate.opsForValue().get(redisKey);
		if (cachedItems != null) {
			return ResponseEntity.ok(cachedItems); // Return cached items
		}

		// Fetch from database if not found in Redis
		List<Item> items = itemRepository.findBySeller(user);

		// Store in Redis for future requests (with optional expiry)
		redisTemplate.opsForValue().set(redisKey, items, Duration.ofMinutes(10));

		return ResponseEntity.ok(items);
	}


//	public ResponseEntity<List<Item>> ListOfSellerItems(String username) {
//		User user = userRepository.findByUsername(username).orElse(null);
//		if (user == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//		}
//
//		List<Item> items = itemRepository.findBySeller(user);
//		return ResponseEntity.ok(items);
//	}

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

		// Delete the item from the database
		itemRepository.delete(item);

		// Remove cached data from Redis to ensure fresh data
		String redisKey = "seller_items:" + user.getId();
		redisTemplate.delete(redisKey);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item deleted successfully");
	}


//	public ResponseEntity<String> deleteSellerItems(String username, Long itemId) {
//		User user = userRepository.findByUsername(username).orElse(null);
//		if (user == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found");
//		}
//
//		Item item = itemRepository.findById(itemId).orElse(null);
//		if (item == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item was not found");
//		}
//
//		if (!item.getSeller().getId().equals(user.getId())) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not authorized to delete this item");
//		}
//
//		itemRepository.delete(item);
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Item deleted successfully");
//	}

	public Item getItem(Long itemId) {
		String redisKey = "item:" + itemId;

		// Check if the item is in Redis
		Item cachedItem = (Item) redisTemplate.opsForValue().get(redisKey);
		if (cachedItem != null) {
			return cachedItem;  // Return cached item
		}

		// Fetch from DB if not in cache
		Item item = itemRepository.findById(itemId).orElse(null);
		if (item != null) {
			// Store in Redis for future requests (Optional: Set expiration time)
			redisTemplate.opsForValue().set(redisKey, item, Duration.ofMinutes(30));
		}

		return item;
	}

}
