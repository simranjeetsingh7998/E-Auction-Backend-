package com.auction.type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionTypeDao extends JpaRepository<AuctionType, Integer> {
	
	List<AuctionType> findAllByIsActiveTrue();

}
