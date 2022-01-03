package com.auction.method;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionMethodDao extends JpaRepository<AuctionMethod, Integer> {
	
	List<AuctionMethod> findAllByIsActiveTrue();

}
