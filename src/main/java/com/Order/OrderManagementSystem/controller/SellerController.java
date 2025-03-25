package com.Order.OrderManagementSystem.controller;


import com.Order.OrderManagementSystem.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;

    @GetMapping("/health")
    public String health(){
        return "great";
    }
    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrdersOfSeller(@AuthenticationPrincipal UserDetails userDetails) {

        String username=userDetails.getUsername();
        return sellerService.getOrdersOfSeller(username);

    }

}
