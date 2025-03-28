package com.Order.OrderManagementSystem.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.Order.OrderManagementSystem.dtos.OrderItemDto;
import com.Order.OrderManagementSystem.entity.Item;
import com.Order.OrderManagementSystem.entity.OrderItem;
import com.Order.OrderManagementSystem.entity.User;
import com.Order.OrderManagementSystem.repository.ItemRepository;
import com.Order.OrderManagementSystem.repository.OrderItemRepository;
import com.Order.OrderManagementSystem.repository.UserRepository;

@Service
public class OrderItemService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;


	public ResponseEntity<?> createOrder(OrderItem orderItems, String username) {
		User customer = userRepository.findByUsername(username).orElse(null);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer");
		}

		List<Long> orderItemIds = orderItems.getItemIds();
		orderItems.setDate(LocalDateTime.now());
		List<OrderItemDto> orderItemDtos = new ArrayList<>();
		Map<User, OrderItem> sellerOrderMap = new HashMap<>();

		for (Long id : orderItemIds) {
			Item item = itemRepository.findById(id).orElse(null);
			if (item != null) {
				orderItemDtos.add(new OrderItemDto(item));

				User seller = item.getSeller();
				sellerOrderMap.putIfAbsent(seller, new OrderItem());
				OrderItem sellerOrder = sellerOrderMap.get(seller);

				sellerOrder.getItemIds().add(item.getId());
				sellerOrder.setCustomer(customer);
				sellerOrder.setSeller(seller);
				sellerOrder.setDate(LocalDateTime.now()); //
			}
		}

		// Save orders and update seller's sellerOrders list
		for (Map.Entry<User, OrderItem> entry : sellerOrderMap.entrySet()) {
			User seller = entry.getKey();
			OrderItem sellerOrder = entry.getValue();

			orderItemRepository.save(sellerOrder);
			seller.getSellerOrders().add(sellerOrder);
			userRepository.save(seller);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(orderItemDtos);
	}


	//paraters are Buyer userName and Item id (requrie to do changes in there order like to delete few items in existing order
	public ResponseEntity<String> updateOrder(String username, Long itemId) {
	    // Fetch User
	    User user = userRepository.findByUsername(username).orElse(null);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
	    }

	    // Fetch the user's existing orders
	    List<OrderItem> orderItems = orderItemRepository.findByCustomer(user);
	    if (orderItems == null || orderItems.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No items found in order");
	    }

	    boolean itemRemoved = false;

	    // Iterate through each order and remove the item if present
	    for (OrderItem orderItem : orderItems) {
	        List<Long> itemIds = new ArrayList<>(orderItem.getItemIds());
	        if (itemIds.remove(itemId)) {
	            orderItem.setItemIds(itemIds);


				Item itm=itemRepository.findById(itemId).orElse(null);
				User seller=itm.getSeller();
				seller.getSellerOrders().remove(itemId);
				user.getCustomerOrders().remove(itemId);

	            orderItemRepository.save(orderItem);  // Save updated order item
				userRepository.save(seller);
				userRepository.save(user);
	            itemRemoved = true;
	        }
	    }

	    if (!itemRemoved) {

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not found in order");

	    }

	    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated successfully");
	}
	
	
	//get all user orders
	public ResponseEntity<?> getUserOrders( String username) {
		
	    User user = userRepository.findByUsername(username).orElse(null);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
	    }
		List<OrderItem> customerOrders=user.getCustomerOrders();
		List<OrderItemDto> items = new ArrayList<>();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(items);
		
	}

	//schedule the deletion
	@Scheduled(fixedRate = 5400000) // 1.5 hours in milliseconds
	public void autoDeleteExpiredOrders() {
		LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(90);
		List<OrderItem> expiredOrders = orderItemRepository.findByDateBefore(expiryTime);

		for (OrderItem orderItem : expiredOrders) {
			User customer = orderItem.getCustomer();
			if (customer != null) {
				customer.getCustomerOrders().remove(orderItem);
				userRepository.save(customer);
			}

			User seller = orderItem.getSeller();
			if (seller != null) {
				seller.getSellerOrders().remove(orderItem);
				userRepository.save(seller);
			}

			orderItemRepository.delete(orderItem);
		}
	}

	//manual deletion

	public ResponseEntity<?> deleteOrder(Long orderItemId, String username) {
		User customer = userRepository.findByUsername(username).orElse(null);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer");
		}

		OrderItem orderItem = orderItemRepository.findById(orderItemId).orElse(null);
		if (orderItem == null || !orderItem.getCustomer().equals(customer)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found or not authorized to delete");
		}

		// Remove from seller's orders list
		User seller = orderItem.getSeller();
		if (seller != null) {
			seller.getSellerOrders().remove(orderItem);
			userRepository.save(seller);
		}

		// Remove from customer's orders list
		customer.getCustomerOrders().remove(orderItem);
		userRepository.save(customer);

		// Delete the order from the repository
		orderItemRepository.delete(orderItem);

		return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully");
	}





}



