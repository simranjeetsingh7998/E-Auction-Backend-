package com.auction.bidder.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBidderCategoryDao extends JpaRepository<BidderCategory, Integer> {
	
	List<BidderCategory> findAllByIsActiveTrueAndBidderTypeId(Integer id);

}
