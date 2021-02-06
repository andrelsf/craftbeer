package com.beerhouse.api.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * To ensure that the controller is being 
 * created within the context of Spring
 * 
 * @since V1
 * @category test
 * @author Andre Ferreira
 *
 */
@SpringBootTest
@ActiveProfiles("test")
class SmokeTest {

	@Autowired
	private BeerResource beerResource;

	@Test
	void contextLoads() throws Exception {
		assertThat(beerResource).isNotNull();
	}
}
