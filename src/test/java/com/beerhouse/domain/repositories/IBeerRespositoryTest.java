package com.beerhouse.domain.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.domain.models.Beer;
import com.beerhouse.domain.models.Category;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IBeerRespositoryTest {

	@Autowired
	private IBeerRespository repository;

	@Autowired
	private TestEntityManager testEntityManager;

	private Beer testBeer;

	@Before
	public void setUp() {
		repository.deleteAll();

		Beer duff = new Beer();
		duff.setName("Duff");
		duff.setIngredients("Hops and Wheat");
		duff.setAlcoholContent("5%");
		duff.setPrice(BigDecimal.valueOf(15.9));
		duff.setCategory(Category.PILSEN);

		testEntityManager.persistAndFlush(duff);

		this.testBeer = duff;
		assertNotNull(duff);
	}

	@After
	public void tearDown() {
		repository.deleteAll();
	}

	@Test
	@DisplayName("Find a beer by name")
	public void test_whenBeerNameIsValid_thenMustReturnABeer() {
		// Given
		String beerName = "Duff";
		Pageable pageable = PageRequest.of(0, 10);

		// When
		Page<Beer> beer = repository.findByNameContaining(beerName, pageable);

		// then
		assertNotNull(beer);
		assertThat(beer.getContent().get(0).getName()).isEqualTo(beerName);
	}

	@Test
	@DisplayName("Get beer by Id valid")
	public void test_whenBeerIdValid_thenMustReturnABeer() {
		// Given
		Long beerId = 1L;

		// When
		Beer beer = repository.getOne(beerId);

		// Then
		assertNotNull(beer);
		assertThat(beer.getBeerId()).isEqualTo(beerId);
	}

	@Test
	@DisplayName("Failed to try to find a beer with invalid id")
	public void test_whenBeerIdInvalid_thenNotReturnABeer() {
		// Given
		Long beerId = 9999L;

		// When
		Optional<Beer> beer = repository.findById(beerId);

		// Then
		assertTrue(beer.isEmpty());
	}

	@Test
	@DisplayName("Can't find a beer, name invalid")
	public void test_whenBeerNameIsInvalid_thenDoNotReturnABeer() {
		// Given
		String beerName = "Panela";
		Pageable pageable = PageRequest.of(0, 10);

		// When
		Page<Beer> beer = repository.findByNameContaining(beerName, pageable);

		// Then
		assertTrue(beer.isEmpty());
	}

	@Test
	@DisplayName("Removes a record")
	public void test_whenBeerIdValid_thenRemoveTheRecord() {
		// When
		repository.deleteById(testBeer.getBeerId());

		// Then
		assertThat(repository.count()).isEqualTo(0);
	}

	@Test
	@DisplayName("Id Invalid throw Exception")
	public void test_whenBeerIdInvalid_thenThrowsEmptyResultDataAccessException() {
		// Given
		Long beerIdInvalid = 9999L;

		// When
		Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(beerIdInvalid);
		});

		String currentMessage = exception.getMessage();
		String expectedMessage = "No class com.beerhouse.domain.models.Beer entity with id " + beerIdInvalid
				+ " exists!";

		// Then
		assertTrue(currentMessage.contains(expectedMessage));
	}

}
