package com.auction.preparation;

import java.util.List;

public interface IAuctionPreparationService {
	
	AuctionPreparationVO save(AuctionPreparationVO auctionPreparationVO);
	
	void mapToTemplate(Long id, Integer templateId);
	
	List<AuctionPreparationVO> searchAuctionPreparation(AuctionPreparationSearchParam auctionPreparationSearchParam);
	
	AuctionPreparationVO findAllDetailsById(Long id);
	
	void publish(Long id);

	void returnAuction(Long auctionPreparationId, ReturnReasonVO returnReasonVO);
	
	List<ReturnReasonVO> returnReasonsByAuctionId(Long auctionId);

}
