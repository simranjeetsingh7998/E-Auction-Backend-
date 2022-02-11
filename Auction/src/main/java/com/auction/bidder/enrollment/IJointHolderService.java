package com.auction.bidder.enrollment;

import java.util.List;

public interface IJointHolderService {
	
	JointHolderVO save(Long bidderAuctionEnrollmentId, JointHolderVO jointHolderVO);
	
	void delete(Long id);
	
	List<JointHolderVO> findAllByBidderAuctionEnrollmentId(Long bidderAuctionEnrollmentId);

}
