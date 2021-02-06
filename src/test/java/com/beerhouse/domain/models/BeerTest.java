package com.beerhouse.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BeerTest {

	@Test
	public void whenInstantiatingAnObject_thenItMustNotBeNull() {
		// Given
		Beer beer = new Beer();
		
		// when
		beer.setName("Duff");
		beer.setIngredients("Hops and Wheat");
		beer.setAlcoholContent("7%");
		beer.setPrice(BigDecimal.valueOf(19.9));
		beer.setCategory(Category.PILSEN);

		// Then
		assertNotNull(beer);
		assertThat(beer.getName()).isEqualTo("Duff");
		assertThat(beer.getIngredients()).isEqualTo("Hops and Wheat");
		assertThat(beer.getAlcoholContent()).isEqualTo("7%");
		assertThat(beer.getPrice()).isEqualTo(BigDecimal.valueOf(19.9));
		assertThat(beer.getCategory()).isEqualTo(Category.PILSEN);
	}

}
