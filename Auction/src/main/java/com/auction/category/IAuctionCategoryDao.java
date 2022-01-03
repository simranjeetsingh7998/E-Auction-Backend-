package com.auction.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionCategoryDao extends JpaRepository<AuctionCategory, Integer> {
	
	List<AuctionCategory> findAllByIsActiveTrue();

}
