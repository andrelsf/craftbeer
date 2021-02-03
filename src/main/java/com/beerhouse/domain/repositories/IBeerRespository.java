package com.beerhouse.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beerhouse.domain.models.Beer;

@Repository
public interface IBeerRespository extends JpaRepository<Beer, Long>{

	Page<Beer> findByNameContaining(String name, Pageable pageable);

}
