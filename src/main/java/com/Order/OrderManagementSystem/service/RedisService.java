package com.Order.OrderManagementSystem.service;
import com.Order.OrderManagementSystem.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String ITEM_KEY_PREFIX = "item:";

    public void saveItemToRedis(Item item, long timeoutInMinutes) {
        redisTemplate.opsForValue().set(ITEM_KEY_PREFIX + item.getId(), item, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public Item getItemFromRedis(Long itemId) {
        return (Item) redisTemplate.opsForValue().get(ITEM_KEY_PREFIX + itemId);
    }

    public void deleteItemFromRedis(Long itemId) {
        redisTemplate.delete(ITEM_KEY_PREFIX + itemId);
    }
}
