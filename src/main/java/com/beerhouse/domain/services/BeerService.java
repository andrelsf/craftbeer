package com.beerhouse.domain.services;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.beerhouse.api.resources.http.request.BeerRequest;
import com.beerhouse.api.resources.http.response.BeerResponse;
import com.beerhouse.domain.models.Beer;
import com.beerhouse.domain.models.Category;
import com.beerhouse.domain.repositories.IBeerRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Service
public class BeerService {

	@Autowired
	private IBeerRespository repository;

	/**
	 * Search the database for all beers
	 * 
	 * @param pageable
	 * @return BeerResponse
	 */
	public Page<BeerResponse> findAll(Pageable pageable) {
		Page<Beer> beers = repository.findAll(pageable);
		return BeerResponse.convert(beers);
	}

	/**
	 * Searches the database for all beers containing the name as a reference
	 * 
	 * @param name
	 * @param pageable
	 * @return BeerResponse
	 */
	public Page<BeerResponse> findByNameContaining(String name, Pageable pageable) {
		Page<Beer> beers = repository.findByNameContaining(name, pageable);
		return BeerResponse.convert(beers);
	}

	/**
	 * Find beer in the database by id
	 * 
	 * @throws EntityNotFoundException
	 * @param beerId
	 * @return Beer
	 */
	public Beer getBeerById(Long beerId) {
		Optional<Beer> beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			return beer.get();
		}
		
		throw new EntityNotFoundException("Beer not found by id: " + beerId);
	}

	/**
	 * Add a new beer in database
	 * 
	 * @param beerRequest
	 * @return Beer
	 */
	@Transactional
	public Beer create(BeerRequest beerRequest) {
		Beer beer = beerRequest.convert();
		repository.save(beer);
		return beer;
	}

	/**
	 * Removes a beer from database
	 * 
	 * @param beerId
	 * @return boolean
	 */
	@Transactional
	public boolean deleteBeerById(Long beerId) {
		Optional<Beer>  beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			repository.deleteById(beerId);
			return true;			
		}
		
		return false;
	}

	/**
	 * Update a beer by id
	 * 
	 * @throws EntityNotFoundException
	 * @param beerId
	 * @param beerRequest
	 * @return Beer
	 */
	@Transactional
	public Beer updateBeerById(Long beerId, BeerRequest beerRequest) {
		Beer beer = repository.getOne(beerId);
		
		if (beer != null) {
			BeanUtils.copyProperties(beerRequest, beer, "beerId", "category");
			beer.setCategory(Category.valueOf(beerRequest.getCategory()));
			return beer;
		}
		
		throw new EntityNotFoundException("Beer not found by id: " + beerId);
	}


	/**
	 * Updates partial content of a beer by id
	 * 
	 * @param beerId
	 * @param fields
	 * @return Beer
	 */
	@Transactional
	public Beer updatePartialContent(Long beerId, Map<String, Object> fields) {
		Optional<Beer> beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			this.merge(fields, beer.get());
			return beer.get();
		}
		
		throw new EntityNotFoundException("Beer not found by id: " + beerId);
	}
	
	/**
	 * Used by the updatePartialContent method to identify received attributes and apply values to the entity
	 * 
	 * An UnrecognizedPropertyException exception can be thrown if the mapping key does not match the class attribute name
	 * 
	 * @throws UnrecognizedPropertyException
	 * @param sourceData
	 * @param beerTarget
	 */
	private void merge(Map<String, Object> sourceData, Beer beerTarget) {
		ObjectMapper objectMapper = new ObjectMapper();
		Beer sourceBeer = objectMapper.convertValue(sourceData, Beer.class);
		
		sourceData.forEach((atribute, value) -> {
			Field field = ReflectionUtils.findField(Beer.class, atribute);
			field.setAccessible(true);
			Object newValue = ReflectionUtils.getField(field, sourceBeer);
			ReflectionUtils.setField(field, beerTarget, newValue);
		});
	}
}
