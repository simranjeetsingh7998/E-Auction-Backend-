package com.auction.division;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDivisionDao extends JpaRepository<Division, Integer> {
	
	List<Division> findAllByIsActiveTrue();

}
