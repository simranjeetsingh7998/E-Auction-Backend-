package com.auction.preparation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.auction.bidding.BiddingVO;
import com.auction.emd.fee.payment.mode.EMDFeePaymentModeVO;
import com.auction.organization.item.OrganizationItemVO;

public interface IAuctionPreparationService {
	
	AuctionPreparationVO save(AuctionPreparationVO auctionPreparationVO);
	
	void mapToTemplate(Long id, Integer templateId);
	
	List<AuctionPreparationVO> searchAuctionPreparation(AuctionPreparationSearchParam auctionPreparationSearchParam);
	
	AuctionPreparationVO findAllDetailsById(Long id);
	
	AuctionPreparationVO getAuctionPreparationDetailsToSchedule(Long id);
	
	void publish(Long id, AuctionPreparationVO auctionPreparationVO);
	
	void schedule(Long id, AuctionScheduleVO auctionScheduleVO);

	void returnAuction(Long auctionPreparationId, ReturnReasonVO returnReasonVO);
	
	List<ReturnReasonVO> returnReasonsByAuctionId(Long auctionId);
	
	AuctionItemVO addAuctionItem(Long auctionPreparationId, String auctionItem, MultipartFile multipartFile) throws IOException;
	
	void deleteAuctionItem(Long auctionPreparationId, Long auctionItemId);
	
	Map<String, String> uploadDocument(Long auctionPreparationId, String documentType, MultipartFile multipartFile) throws IOException;
	
	Map<String, Object> getAuctionDocument(Long auctionPreparationId, String documentType);
	
	List<AuctionPreparationVO> findAuctionByStatus(String status);
	
	Map<Integer, OrganizationItemVO> findOrganizationItemsByAuctionIdAndItemId(Long auctionId, Long itemId);

	List<BiddingVO> userCurrentAuctions(List<Long> auctionIds);
	
	List<AuctionPreparationVO> findAllBidderUpcomingAuctions();
	
	List<AuctionDocumentVO> findAllAuctionDocuments();
	
	EMDFeePaymentModeVO getEmdFeePaymentModeByAuction(Long auctionId);

	List<BiddingVO> liveAuctionsOnAdmin(List<Long> auctionsId);
	
	String findAuctionMethodByAuctionId(Long auctionId);

}
