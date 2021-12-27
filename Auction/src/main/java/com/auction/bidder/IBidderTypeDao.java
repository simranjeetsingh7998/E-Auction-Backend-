package com.auction.bidder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBidderTypeDao extends JpaRepository<BidderType, Integer> {
	
	List<BidderType> findAllByIsActiveTrue();

}
