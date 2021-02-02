package com.beerhouse.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private String name;
	private String ingredients;
	private String alcoholContent;
	private BigDecimal price;
	
	@Enumerated(EnumType.STRING)
	private Category category;
	
	private LocalDateTime createAt = LocalDateTime.now();
	private LocalDateTime updateAt = LocalDateTime.now();
	
}
