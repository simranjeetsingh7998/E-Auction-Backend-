package com.auction.process;

import java.util.List;

public interface IAuctionProcessService {
	
	List<AuctionProcessVO> findAllByIsActiveTrue();
	
	void addOrUpdate(AuctionProcessVO auctionProcessVO);
	
	AuctionProcessVO findById(Integer id);
	
	void deActivate(Integer id);

}
