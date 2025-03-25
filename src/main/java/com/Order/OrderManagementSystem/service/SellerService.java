package com.Order.OrderManagementSystem.service;
import com.Order.OrderManagementSystem.dtos.OrderItemDto;
import com.Order.OrderManagementSystem.entity.Item;
import com.Order.OrderManagementSystem.entity.OrderItem;
import com.Order.OrderManagementSystem.entity.User;
import com.Order.OrderManagementSystem.repository.ItemRepository;
import com.Order.OrderManagementSystem.repository.OrderItemRepository;
import com.Order.OrderManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    public ResponseEntity<?> getOrdersOfSeller(String username) {

        User seller= userRepository.findByUsername(username).orElse(null);



        if(seller==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not a seller or atleast not a user");
        }

        List<OrderItem> ordersForSeller=seller.getSellerOrders();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ordersForSeller);
    }




}




