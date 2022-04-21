package com.auction.bidder.enrollment;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IBidderAuctionEnrollmentService {
	
	BidderAuctionEnrollmentVO save(String bidderAuctionEnrollmentVO, MultipartFile multipartFile) throws IOException;
	
	List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId);
	
	void uploadBidderAuctionEnrollmentDocument(Long bidderAuctionEnrollmentId, String documentType, MultipartFile file) throws IOException;

}
