package com.Order.OrderManagementSystem.service;
import com.Order.OrderManagementSystem.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ItemRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ITEM_KEY_PREFIX = "ITEM:";

    // Save an item to Redis with an optional TTL (e.g., 10 minutes)
    public void saveItem(Item item) {
        redisTemplate.opsForValue().set(ITEM_KEY_PREFIX + item.getId(), item, 10, TimeUnit.MINUTES);
    }

    // Get an item from Redis
    public Item getItemById(Long id) {
        return (Item) redisTemplate.opsForValue().get(ITEM_KEY_PREFIX + id);
    }

    // Delete an item from Redis
    public void deleteItem(Long id) {
        redisTemplate.delete(ITEM_KEY_PREFIX + id);
    }
}
