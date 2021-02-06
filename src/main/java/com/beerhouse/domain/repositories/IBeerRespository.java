package com.beerhouse.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beerhouse.domain.models.Beer;

@Repository
public interface IBeerRespository extends JpaRepository<Beer, Long>{

	//@Query("SELECT beer_id, name, ingredients, alcohol_content, price, category FROM Beer b WHERE b.name LIKE '%:title%'")
	Page<Beer> findByNameContaining(String name, Pageable pageable);

}
