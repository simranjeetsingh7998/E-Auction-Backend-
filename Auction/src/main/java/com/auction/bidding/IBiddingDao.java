package com.auction.bidding;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;

@Repository
public interface IBiddingDao extends JpaRepository<Bidding, Long> {
	
	@Query("select b.biddingAmount from Bidding b where b.auctionPreparation = ?1")
	Optional<Double> findBiddingAmountByAuctionPreparationAndRoundNo(AuctionPreparation auctionPreparation, String roundNo, PageRequest pageRequest);
	
	@Query("from Bidding b join fetch b.bidder where b.auctionPreparation = ?1 and roundNo = ?2")
	Optional<Bidding> findByAuctionPreparationAndRoundNo(AuctionPreparation auctionPreparation,String roundNo, PageRequest pageRequest);
	
	@Query("from Bidding b join fetch b.bidder where b.auctionPreparation = ?1")
	Optional<Bidding> findByAuctionPreparation(AuctionPreparation auctionPreparation, PageRequest pageRequest);

}
