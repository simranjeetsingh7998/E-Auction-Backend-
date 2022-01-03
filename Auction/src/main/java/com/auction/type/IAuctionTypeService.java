package com.auction.type;

import java.util.List;

public interface IAuctionTypeService {
	
	List<AuctionTypeVO> findAllByIsActiveTrue();
	
	void addOrUpdate(AuctionTypeVO auctionTypeVO);
	
	AuctionTypeVO findById(Integer id);
	
	void deActivate(Integer id);

}
