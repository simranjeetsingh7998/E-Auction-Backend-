package com.auction.station;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStationDao extends JpaRepository<Station, Integer> {
	
	List<Station> findAllByIsActiveTrue();

}
