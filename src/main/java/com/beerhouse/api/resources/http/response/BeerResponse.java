package com.beerhouse.api.resources.http.response;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.beerhouse.domain.models.Beer;

import lombok.Getter;

@Getter
public class BeerResponse {

	private Long beerId;
	private String name;
	private String ingredients;
	private String alcoholContent;
	private BigDecimal price;
	private String category;
	
	public BeerResponse(Beer beer) {
		this.beerId = beer.getBeerId();
		this.name = beer.getName();
		this.ingredients = beer.getIngredients();
		this.alcoholContent = beer.getAlcoholContent();
		this.price = beer.getPrice();
		this.category = beer.getCategory().toString();
	}
	
	public static Page<BeerResponse> convert(Page<Beer> beers) {
		return beers.map(BeerResponse::new);
	}
}
