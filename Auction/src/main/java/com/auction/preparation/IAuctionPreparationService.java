package com.auction.preparation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IAuctionPreparationService {
	
	AuctionPreparationVO save(AuctionPreparationVO auctionPreparationVO);
	
	void mapToTemplate(Long id, Integer templateId);
	
	List<AuctionPreparationVO> searchAuctionPreparation(AuctionPreparationSearchParam auctionPreparationSearchParam);
	
	AuctionPreparationVO findAllDetailsById(Long id);
	
	void publish(Long id);

	void returnAuction(Long auctionPreparationId, ReturnReasonVO returnReasonVO);
	
	List<ReturnReasonVO> returnReasonsByAuctionId(Long auctionId);
	
	AuctionItemVO addAuctionItem(Long auctionPreparationId, String auctionItem, MultipartFile multipartFile) throws IOException;
	
	void deleteAuctionItem(Long auctionPreparationId, Long auctionItemId);
	
	Map<String, String> uploadDocument(Long auctionPreparationId, String documentType, MultipartFile multipartFile) throws IOException;

}
