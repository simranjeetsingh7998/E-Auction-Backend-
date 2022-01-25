package com.auction.preparation;

import java.util.List;

public interface IAuctionPreparationService {
	
	AuctionPreparationVO save(AuctionPreparationVO auctionPreparationVO);
	
	void mapToTemplate(Long id, Integer templateId);
	
	List<AuctionPreparationVO> searchAuctionPreparation(AuctionPreparationSearchParam auctionPreparationSearchParam);
	
	List<AuctionPreparationVO> findAllDetailsById(Long id);

}
