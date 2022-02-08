package com.auction.bidder.enrollment;

import java.util.List;

public interface IBidderAuctionEnrollmentService {
	
	public void save(BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO);
	
	List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId);

}
