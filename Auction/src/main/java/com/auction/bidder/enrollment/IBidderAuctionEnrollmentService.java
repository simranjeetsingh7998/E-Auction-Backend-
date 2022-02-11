package com.auction.bidder.enrollment;

import java.util.List;

public interface IBidderAuctionEnrollmentService {
	
	BidderAuctionEnrollmentVO save(BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO);
	
	List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId);

}
