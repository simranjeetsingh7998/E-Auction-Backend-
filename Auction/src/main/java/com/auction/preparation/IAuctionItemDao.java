package com.auction.preparation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionItemDao extends JpaRepository<AuctionItem, Long> {
	
	void deleteByIdAndAuctionPreparation(Long auctionItemId, AuctionPreparation auctionPreparation);
	
	Optional<AuctionItem> findByIdAndAuctionPreparation(Long id, AuctionPreparation auctionPreparation);
 
}
