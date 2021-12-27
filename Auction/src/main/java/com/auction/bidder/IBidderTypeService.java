package com.auction.bidder;

import java.util.List;

public interface IBidderTypeService {
	
	List<BidderTypeVO> findAllActiveBidderTypes();
	
	void saveBidderType(BidderTypeVO bidderTypeVO);
	
	BidderTypeVO findBidderTypeById(Integer id);

}
