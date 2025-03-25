package com.Order.OrderManagementSystem.dtos;

import com.Order.OrderManagementSystem.entity.Item;
import com.Order.OrderManagementSystem.enums.Size;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderItemDto {
	
	private Long id;
	
	private String name;
	private Integer price;
	
	private Size size;
	
	
	public OrderItemDto(Item item) {
		
		this.id=item.getId();
		this.name=item.getName();
		this.price=item.getPrice();
		this.size=item.getSize();
		
	}
	
}
