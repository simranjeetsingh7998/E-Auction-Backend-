package com.auction.bidding;

import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;
import com.auction.user.User;

@Repository
public interface IBiddingDao extends JpaRepository<Bidding, Long> {
	
	@Query("select b.biddingAmount from Bidding b where b.auctionPreparation = ?1")
	Optional<Double> findBiddingAmountByAuctionPreparationAndRoundNo(AuctionPreparation auctionPreparation, String roundNo, PageRequest pageRequest);
	
	@Query("from Bidding b join fetch b.bidder where b.auctionPreparation = ?1 and roundNo = ?2")
	Optional<Bidding> findByAuctionPreparationAndRoundNo(AuctionPreparation auctionPreparation,String roundNo, PageRequest pageRequest);
	
	@Query("from Bidding b join fetch b.bidder where b.auctionPreparation = ?1")
	Optional<Bidding> findByAuctionPreparation(AuctionPreparation auctionPreparation, PageRequest pageRequest);
	
	long countByAuctionPreparationAndBidderAndRoundClosedAtIsNotNull(AuctionPreparation auctionPreparation, User bidder);
	
	@Query(value =  "select u.first_name as firstName,u.last_name as lastName, b.bidding_amount as amount,b.round_no as round, b.bidding_at as bidAt from bidding b join users u on  b.bidder_id= u.id where b.auction_preparation_id = ?1", nativeQuery = true)
	List<Tuple> findBidHistoryByAuctionPreparation(Long auctionId);

}
