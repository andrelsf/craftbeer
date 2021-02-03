package com.beerhouse.api.resources;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.beerhouse.api.resources.http.request.BeerRequest;
import com.beerhouse.api.resources.http.response.BeerResponse;
import com.beerhouse.domain.models.Beer;
import com.beerhouse.domain.services.BeerService;

@RestController
@RequestMapping(value = BaseResource.BEER_V1)
public class BeerResource {

	@Autowired
	private BeerService service;

	@GetMapping
	public Page<BeerResponse> getAllBeers(@RequestParam(required = false) String name,
			@PageableDefault(sort = "beerId", direction = Direction.ASC, page = 0, size = 10) Pageable pageable) {
		if (name == null) {
			Page<BeerResponse> beers = service.findAll(pageable);
			return beers;
		}

		Page<BeerResponse> beers = service.findByNameContaining(name, pageable);
		return beers;
	}

	@GetMapping("/{beerId}")
	public ResponseEntity<BeerResponse> getBeerById(@PathVariable Long beerId) {
		Beer beer = service.getBeerById(beerId);

		return ResponseEntity.ok(new BeerResponse(beer));
	}

	@PostMapping
	public ResponseEntity<BeerResponse> create(@RequestBody @Valid BeerRequest beerRequest,
			UriComponentsBuilder uriBuilder) {
		Beer beer = service.create(beerRequest);

		URI uriLocation = uriBuilder.path(BaseResource.BEER_V1 + "/{beerId}")
				.buildAndExpand(beer.getBeerId().toString()).toUri();

		return ResponseEntity.created(uriLocation).body(new BeerResponse(beer));
	}

	@PutMapping("/{beerId}")
	public ResponseEntity<BeerResponse> updateBeerById(@PathVariable Long beerId,
			@RequestBody @Valid BeerRequest beerRequest) {
		Beer beer = service.updateBeerById(beerId, beerRequest);

		return ResponseEntity.ok(new BeerResponse(beer));
	}

	
	@PatchMapping("/{beerId}")
	public ResponseEntity<?> updateNameBeer(@PathVariable Long beerId, @RequestBody Map<String, Object> fields) {
		service.updatePartialContent(beerId, fields);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{beerId}")
	public ResponseEntity<BeerResponse> deleteBeerById(@PathVariable Long beerId) {
		if (service.deleteBeerById(beerId)) {
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}
}


















