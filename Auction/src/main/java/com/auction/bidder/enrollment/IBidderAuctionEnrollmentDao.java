package com.auction.bidder.enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;
import com.auction.user.User;

@Repository
public interface IBidderAuctionEnrollmentDao extends JpaRepository<BidderAuctionEnrollment, Long> {

	List<BidderAuctionEnrollment> findAllByAuctionPreparation(AuctionPreparation auctionPreparation);
	
	@Query("select b.emdLimit from BidderAuctionEnrollment b where b.auctionPreparation=?1 and b.user =?2")
	Integer findRoundParticipantCount(AuctionPreparation auctionPreparation, User participant);
	
	@Query("select sum(b.emdLimit) from BidderAuctionEnrollment b where b.auctionPreparation=?1")
	int totalEmdCountByAuctionPreparationId(AuctionPreparation auctionPreparation);
	
	long countByAuctionPreparation(AuctionPreparation auctionPreparation);
	
	boolean existsByAuctionPreparationAndUser(AuctionPreparation auctionPreparation, User participant);
	
}
