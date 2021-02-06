package com.beerhouse.api.resources;

import static com.beerhouse.api.resources.BaseResource.BEERS_V1;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.beerhouse.api.resources.http.request.BeerRequest;
import com.beerhouse.domain.models.Beer;
import com.beerhouse.domain.models.Category;
import com.beerhouse.domain.repositories.IBeerRespository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BeerResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private IBeerRespository repository;

	@Test
	@DisplayName("GET [/unknown/uri] : Return not found")
	public void test_UnknownURI() throws Exception {
		URI unknownURI = new URI("/unknown/uri");
		
		this.mockMvc
			.perform(get(unknownURI)
					.header(ACCEPT, APPLICATION_JSON_VALUE)
					.contentType(APPLICATION_JSON))
					.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("GET " + BEERS_V1 + " : Get all resources")
	public void test_whenTheMethodIsGET_thenStatusCode200MustBeReturned() throws Exception {
		URI beerUri = new URI(BEERS_V1);

		this.mockMvc
				.perform(get(beerUri)
					.header(ACCEPT, APPLICATION_JSON_VALUE)
					.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.first", is(true)))
				.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	@DisplayName("GET " + BEERS_V1 + " : Return entity not found (Id Invalid)")
	public void test_whenTheMethodIsGetAndWithParamterIdInvalid_thenReturnEntityNotFound() throws Exception {
		URI beerUri = new URI(BEERS_V1 + "/9999");

		this.mockMvc.perform(
					get(beerUri)
						.header(ACCEPT, APPLICATION_JSON_VALUE)
						.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.apierror.message", is("Beer not found by id: 9999")))
				.andExpect(jsonPath("$.apierror.status", is("NOT_FOUND")));
	}
	
	@Test
	@DisplayName("GET " + BEERS_V1 + " : Return a single resource by id")
	public void test_whenTheParameterIdIsValidAndTheMetGET_thenReturnASigleResource() throws Exception {
		Beer duff = new Beer();
		duff.setName("Duff");
		duff.setIngredients("Hops and Wheat");
		duff.setAlcoholContent("5%");
		duff.setPrice(BigDecimal.valueOf(15.9));
		duff.setCategory(Category.PILSEN);
		
		Beer beerSaved = repository.save(duff);
		
		URI beerUri = new URI(BEERS_V1 + "/" + beerSaved.getBeerId().toString());
		
		this.mockMvc.perform(
				get(beerUri)
					.header(ACCEPT, APPLICATION_JSON_VALUE)
					.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.beerId").value(beerSaved.getBeerId().toString()))
				.andExpect(jsonPath("$.name", is(beerSaved.getName())))
				.andExpect(jsonPath("$.ingredients", is(beerSaved.getIngredients())))
				.andExpect(jsonPath("$.alcoholContent", is(beerSaved.getAlcoholContent())))
				.andExpect(jsonPath("$.price").value(beerSaved.getPrice().toString()))
				.andExpect(jsonPath("$.category", is(beerSaved.getCategory().toString())));
		
		repository.deleteAll();
	}

	@Test
	@DisplayName("DELETE " + BEERS_V1 + " : Remove the resource by id")
	public void test_whenTheParameterIdIsValidAndTheMetDELETE_thenRemoveTheResource() throws Exception {
		Beer duff = new Beer();
		duff.setName("Duff");
		duff.setIngredients("Hops and Wheat");
		duff.setAlcoholContent("5%");
		duff.setPrice(BigDecimal.valueOf(15.9));
		duff.setCategory(Category.PILSEN);
		
		Beer beerSaved = repository.save(duff);
		
		URI beerUri = new URI(BEERS_V1 + "/" + beerSaved.getBeerId().toString());
		
		this.mockMvc.perform(
				delete(beerUri)
					.header(ACCEPT, APPLICATION_JSON_VALUE)
					.contentType(APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("PUT " + BEERS_V1 + " : Update a single resource by id")
	public void test_whenTheRequestOfTypePUTWithValidId_thenUpdateASingleResource() throws Exception {
		Beer duff = new Beer();
		duff.setName("Duff");
		duff.setIngredients("Hops and Wheat");
		duff.setAlcoholContent("5%");
		duff.setPrice(BigDecimal.valueOf(15.9));
		duff.setCategory(Category.PILSEN);
		
		Beer beerSaved = repository.save(duff);
		
		URI beerUri = new URI(BEERS_V1 + "/" + beerSaved.getBeerId().toString());
		
		Beer duffUpdated = new Beer();
		duffUpdated.setName("Duff Beer");
		duffUpdated.setIngredients("Smooth yeast, Hops and Wheat");
		duffUpdated.setAlcoholContent("7%");
		duffUpdated.setPrice(BigDecimal.valueOf(19.9));
		duffUpdated.setCategory(Category.VIENNA_LAGER);
		
		this.mockMvc.perform(
				put(beerUri)
					.content(jsonToString(duffUpdated))
					.header(ACCEPT, APPLICATION_JSON_VALUE)
					.contentType(APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("PATCH [" + BEERS_V1 + "] :  Partially update the content by id")
	public void test_whenTheRequestOfTypePATCHWithValidId_thenPartiallyUpdateTheContentOfASingleResource() throws Exception {
		Beer duff = new Beer();
		duff.setName("Duff");
		duff.setIngredients("Hops and Wheat");
		duff.setAlcoholContent("5%");
		duff.setPrice(BigDecimal.valueOf(15.9));
		duff.setCategory(Category.PILSEN);
		
		Beer beerSaved = repository.save(duff);
		
		HashMap<String, Object> fields = new HashMap<>();
		fields.put("name", "Duff Beer Update");
		
		URI beerUri = new URI(BEERS_V1 + "/" + beerSaved.getBeerId().toString());
		
		this.mockMvc.perform(
				patch(beerUri)
					.content(jsonToString(fields))
					.header(ACCEPT, APPLICATION_JSON_VALUE)
					.contentType(APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("POST " + BEERS_V1 + " : Create a new resource")
	public void test_whenReceivingAValidPOSTRequest_thenCreateANewResource() throws Exception {
		BeerRequest beer1 = new BeerRequest();
		beer1.setName("Duff");
		beer1.setIngredients("Hops and Wheat");
		beer1.setAlcoholContent("7%");
		beer1.setPrice(BigDecimal.valueOf(19.9));
		beer1.setCategory(Category.PILSEN.toString());
		
		URI beerUri = new URI(BEERS_V1);
		
		this.mockMvc
				.perform(
					post(beerUri)
						.content(jsonToString(beer1))
						.header(ACCEPT, APPLICATION_JSON_VALUE)
						.contentType(APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(APPLICATION_JSON));
		
		repository.deleteAll();
	}
	
	@SuppressWarnings("unused")
	private static <T> Object convertJSONStringToObject(String json, Class<T> clazz) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	    JavaTimeModule module = new JavaTimeModule();
	    mapper.registerModule(module);
	    return mapper.readValue(json, clazz);
	}
	
	private static String jsonToString(final Object object) throws Exception{
		return new ObjectMapper().writeValueAsString(object);
	}

}
