package com.auction.bidder.enrollment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IBidderAuctionEnrollmentService {
	
	BidderAuctionEnrollmentVO save(String bidderAuctionEnrollmentVO, MultipartFile multipartFile) throws IOException;
	
	List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId);
	
	void uploadBidderAuctionEnrollmentDocument(Long bidderAuctionEnrollmentId, String documentType, MultipartFile file) throws IOException;
	
	BidderAuctionEnrollmentVO updateBidderAuctionEnrollment(Long bidderAuctionEnrollmentId,
			String bidderAuctionEnrollmentJson, MultipartFile document) throws IOException;
	
	BidderAuctionEnrollmentVO findByAuctionIdAndBidder(Long auctionId);

	void bidderAuctionEnrollmentVerification(
			BidderEnrollmentVerificationVO bidderEnrollmentVerificationVO);
	
	Map<String, Object> getDocumentForBidder(Long auctionId, Long bidderId, String documentType);

}
