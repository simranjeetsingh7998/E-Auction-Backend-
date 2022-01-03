package com.auction.bid.submission.placement;

import java.util.List;

public interface IBidSubmissionPlacementService {
	
	List<BidSubmissionPlacementVO> findAllByIsActiveTrue();
	
	void addOrUpdate(BidSubmissionPlacementVO bidSubmissionPlacementVO);
	
	BidSubmissionPlacementVO findById(Integer id);
	
	void deActivate(Integer id);

}
