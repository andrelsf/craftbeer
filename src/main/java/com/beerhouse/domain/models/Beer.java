package com.beerhouse.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_beers")
public class Beer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long beerId;
	
	@Column(nullable = false, length = 180)
	private String name;
	
	@Column(nullable = false, length = 400)
	private String ingredients;
	
	@Column(nullable = false, length = 5)
	private String alcoholContent;
	
	@Column(nullable = false)
	private BigDecimal price;
	
	@Enumerated(EnumType.STRING)
	private Category category;
	
	@CreationTimestamp
	private LocalDateTime createAt = LocalDateTime.now();
	
	@UpdateTimestamp
	private LocalDateTime updateAt = LocalDateTime.now();
	
}
