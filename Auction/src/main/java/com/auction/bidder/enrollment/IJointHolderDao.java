package com.auction.bidder.enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJointHolderDao extends JpaRepository<JointHolder, Long> {

	List<JointHolder> findAllByBidderAuctionEnrollment(BidderAuctionEnrollment bidderAuctionEnrollment);
	
}
