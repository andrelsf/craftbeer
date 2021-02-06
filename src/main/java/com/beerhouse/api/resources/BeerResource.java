package com.beerhouse.api.resources;

import static com.beerhouse.api.resources.BaseResource.BEERS_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile(value = {"dev", "test"})
@RequestMapping(value = BEERS_V1, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class BeerResource {

	@Autowired
	private BeerService service;

	/**
	 * Collection Resource List of all paginated resource By default page 0 of size
	 * 10
	 * 
	 * @param name     optional
	 * @param pageable
	 * @return Page<BeerResponse>
	 */
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

	/**
	 * Single-resource Get single resource by id.
	 * 
	 * @param beerId
	 * @return
	 * @throws EntityNotFoundException
	 */
	@GetMapping("/{beerId}")
	public ResponseEntity<BeerResponse> getBeerById(@PathVariable Long beerId) throws EntityNotFoundException {
		Beer beer = service.getBeerById(beerId);

		return ResponseEntity.ok(new BeerResponse(beer));
	}

	/**
	 * Add new resource
	 * 
	 * @param beerRequest
	 * @param uriBuilder
	 * @return ResponseEntity<BeerResponse>
	 */
	@PostMapping
	public ResponseEntity<BeerResponse> create(@RequestBody @Valid BeerRequest beerRequest,
			UriComponentsBuilder uriBuilder) {
		Beer beer = service.create(beerRequest);

		URI uriLocation = uriBuilder.path(BEERS_V1 + "/{beerId}")
				.buildAndExpand(beer.getBeerId().toString()).toUri();

		return ResponseEntity.created(uriLocation).body(new BeerResponse(beer));
	}

	/**
	 * Update single resource by id
	 * 
	 * @param beerId
	 * @param beerRequest
	 * @return NO_CONTENT 409
	 * @throws EntityNotFoundException
	 */
	@PutMapping("/{beerId}")
	public ResponseEntity<?> updateBeerById(@PathVariable Long beerId,
			@RequestBody @Valid BeerRequest beerRequest) throws EntityNotFoundException {
		service.updateBeerById(beerId, beerRequest);

		return ResponseEntity.noContent().build();
	}

	/**
	 * Update partial content of a resource
	 * 
	 * @param beerId
	 * @param fields
	 * @return ResponseEntity<?>
	 */
	@PatchMapping("/{beerId}")
	public ResponseEntity<?> updateNameBeer(@PathVariable Long beerId, @RequestBody Map<String, Object> fields) {
		service.updatePartialContent(beerId, fields);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Removes a resource by id
	 * 
	 * @param beerId
	 * @return
	 * @throws EntityNotFoundException
	 */
	@DeleteMapping("/{beerId}")
	public ResponseEntity<BeerResponse> deleteBeerById(@PathVariable Long beerId) throws EntityNotFoundException {
		if (service.deleteBeerById(beerId)) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
}
