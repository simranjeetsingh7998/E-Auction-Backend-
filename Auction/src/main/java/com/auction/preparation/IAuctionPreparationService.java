package com.auction.preparation;

public interface IAuctionPreparationService {
	
	void save(AuctionPreparationVO auctionPreparationVO);
	
	void mapToTemplate(Long id, Integer templateId);

}
