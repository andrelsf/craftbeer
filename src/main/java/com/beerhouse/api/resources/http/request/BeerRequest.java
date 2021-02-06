package com.beerhouse.api.resources.http.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.beerhouse.core.validation.enums.constraints.ValueOfEnum;
import com.beerhouse.domain.models.Beer;
import com.beerhouse.domain.models.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeerRequest {

	@NotNull
	@NotEmpty
	@Size(min = 4, max = 180)
	private String name;

	@NotNull
	@NotEmpty
	@Size(min = 5, max = 400)
	private String ingredients;

	@NotNull
	@NotEmpty
	@Size(min = 2, max = 5)
	private String alcoholContent;

	@NotNull
	@PositiveOrZero
	private BigDecimal price;

	@NotNull
	@NotEmpty
	@ValueOfEnum(enumClass = Category.class)
	private String category;

	public Beer convert() {
		Beer beer = new Beer();

		beer.setName(name);
		beer.setIngredients(ingredients);
		beer.setAlcoholContent(alcoholContent);
		beer.setPrice(price);
		beer.setCategory(Category.valueOf(category));

		return beer;
	}

}
