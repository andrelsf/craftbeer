package com.beerhouse.domain.services;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;

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

@Service
public class BeerService {

	@Autowired
	private IBeerRespository repository;

	public Page<BeerResponse> findAll(Pageable pageable) {
		Page<Beer> beers = repository.findAll(pageable);
		return BeerResponse.convert(beers);
	}

	public Page<BeerResponse> findByNameContaining(String name, Pageable pageable) {
		Page<Beer> beers = repository.findByNameContaining(name, pageable);
		return BeerResponse.convert(beers);
	}

	public Beer getBeerById(Long beerId) {
		Optional<Beer> beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			return beer.get();
		}
		
		throw new EntityNotFoundException("Beer not found");
	}

	@Transactional
	public Beer create(BeerRequest beerRequest) {
		Beer beer = beerRequest.convert();
		repository.save(beer);
		return beer;
	}

	@Transactional
	public boolean deleteBeerById(Long beerId) {
		Optional<Beer>  beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			repository.deleteById(beerId);
			return true;			
		}
		
		return false;
	}

	@Transactional
	public Beer updateBeerById(Long beerId, BeerRequest beerRequest) {
		Beer beer = repository.getOne(beerId);
		
		if (beer != null) {
			BeanUtils.copyProperties(beerRequest, beer, "beerId", "category");
			beer.setCategory(Category.valueOf(beerRequest.getCategory()));
			return beer;
		}
		
		throw new EntityNotFoundException("Beer not found");
	}


	@Transactional
	public Beer updatePartialContent(Long beerId, Map<String, Object> fields) {
		Optional<Beer> beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			this.merge(fields, beer.get());
			return beer.get();
		}
		
		throw new EntityNotFoundException("Beer not found");
	}
	
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
