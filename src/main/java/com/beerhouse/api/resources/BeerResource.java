package com.beerhouse.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beerhouse.api.resources.http.response.BeerResponse;
import com.beerhouse.domain.services.BeerService;

@RestController
@RequestMapping(value = BaseResource.BEER_V1)
public class BeerResource {

	@Autowired
	private BeerService service;
	
	@GetMapping
	public Page<BeerResponse> getAll(@RequestParam(required = false) String name,
			@PageableDefault(sort = "beerId", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		if (name == null) {
			Page<BeerResponse> beers = service.findAll(pageable);
			return beers;
		}
		
		Page<BeerResponse> beers = service.findByNameContaining(name, pageable);
		return beers;
	}
}
