package com.Order.OrderManagementSystem.entity;

import com.Order.OrderManagementSystem.enums.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Seller who owns this item
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "seller_id", nullable = false)
	private User seller;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Integer price;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Size size;

}





