package com.auction.sector;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISectorDao extends JpaRepository<Sector, Integer> {
	
	List<Sector> findAllByIsActiveTrue();

}
