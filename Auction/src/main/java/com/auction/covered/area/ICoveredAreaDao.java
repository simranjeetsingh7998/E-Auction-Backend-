package com.auction.covered.area;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoveredAreaDao extends JpaRepository<CoveredArea, Integer> {
	
	List<CoveredArea> findAllByIsActiveTrue();

}
