//package com.Order.OrderManagementSystem.scheduler;
//
//import com.Order.OrderManagementSystem.entity.OrderItem;
//import com.Order.OrderManagementSystem.entity.User;
//import com.Order.OrderManagementSystem.repository.OrderItemRepository;
//import com.Order.OrderManagementSystem.repository.UserRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//public class OrderCleanupScheduler {
//
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Scheduled task runs every 10 minutes
//    @Scheduled(fixedRate = 600000) // Runs every 10 minutes
//    public void deleteExpiredOrders() {
//        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(1).minusMinutes(30);
//
//        List<OrderItem> expiredOrders = orderItemRepository.findByDateBefore(cutoffTime);
//
//        if (!expiredOrders.isEmpty()) {
//            for (OrderItem order : expiredOrders) {
//                User seller = order.getSeller();
//                User customer = order.getCustomer();
//
//                // Remove order reference from seller and customer
//                if (seller != null && seller.getSellerOrders() != null) {
//                    seller.getSellerOrders().remove(order);
//                    userRepository.save(seller);
//                }
//                if (customer != null && customer.getCustomerOrders() != null) {
//                    customer.getCustomerOrders().remove(order);
//                    userRepository.save(customer);
//                }
//            }
//
//            // **Optimized Deletion**: Delete all orders in **one batch operation**
//            orderItemRepository.deleteAll(expiredOrders);
//
//            System.out.println(expiredOrders.size() + " expired orders deleted.");
//        }
//    }
//}
