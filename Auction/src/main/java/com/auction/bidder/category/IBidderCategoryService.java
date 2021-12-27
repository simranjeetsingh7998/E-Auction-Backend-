package com.auction.bidder.category;

import java.util.List;

public interface IBidderCategoryService {
	
	void save(BidderCategoryVO bidderCategoryVO);
	
	List<BidderCategoryVO> findByBidderTypeId(Integer id);
	
	List<BidderCategoryVO> findAll();

}
