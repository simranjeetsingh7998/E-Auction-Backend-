package com.auction.category;

import java.util.List;

public interface IAuctionCategoryService {
	
	List<AuctionCategoryVO> findAllByIsActiveTrue();
	
	void addOrUpdate(AuctionCategoryVO auctionCategoryVO);
	
	AuctionCategoryVO findById(Integer id);
	
	void deActivate(Integer id);

}
