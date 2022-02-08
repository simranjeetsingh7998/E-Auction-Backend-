package com.auction.bidder.enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;

@Repository
public interface IBidderAuctionEnrollmentDao extends JpaRepository<BidderAuctionEnrollment, Long> {

	List<BidderAuctionEnrollment> findAllByAuctionPreparation(AuctionPreparation auctionPreparation);
	
}
