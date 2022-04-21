package com.auction.method;

import java.util.List;

public interface IAuctionMethodService {
	
	List<AuctionMethodVO> findAllByIsActiveTrue();
	
	void addOrUpdate(AuctionMethodVO auctionMethodVO);
	
	AuctionMethodVO findById(Integer id);
	
	void deActivate(Integer id);
	
	AuctionMethodVO getAuctionMethodForAuction(Long auctionPreparationId);

}
