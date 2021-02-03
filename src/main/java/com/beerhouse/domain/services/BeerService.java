package com.beerhouse.domain.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.beerhouse.api.resources.http.request.BeerRequest;
import com.beerhouse.api.resources.http.response.BeerResponse;
import com.beerhouse.domain.models.Beer;
import com.beerhouse.domain.repositories.IBeerRespository;

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

	public boolean deleteBeerById(Long beerId) {
		Optional<Beer>  beer = repository.findById(beerId);
		
		if (beer.isPresent()) {
			repository.deleteById(beerId);
			return true;			
		}
		
		return false;
	}
	
	
}
