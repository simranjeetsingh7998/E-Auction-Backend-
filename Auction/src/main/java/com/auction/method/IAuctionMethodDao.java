package com.auction.method;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;

@Repository
public interface IAuctionMethodDao extends JpaRepository<AuctionMethod, Integer> {
	
	List<AuctionMethod> findAllByIsActiveTrue();
	
	List<AuctionMethod> findAllByAuctionPreparations(AuctionPreparation auctionPreparation);
	
	List<AuctionMethod> findAllByMethodAndIsActiveTrue(String method);

}
