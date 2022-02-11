package com.auction.bidder.enrollment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.util.LoggedInUser;

@Service
@Transactional
public class BidderAuctionEnrollmentService implements IBidderAuctionEnrollmentService {
	
	@Autowired
	private IBidderAuctionEnrollmentDao bidderAuctionEnrollmentDao;
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Override
	public BidderAuctionEnrollmentVO save(BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO) {
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(bidderAuctionEnrollmentVO.getAuctionPreparation().getId())
		 .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		LocalDateTime currentDateTime =  LocalDateTime.now();
		if(auctionPreparation.getRegistrationStartDateTime().isBefore(currentDateTime)) {
			throw new DataMisMatchException("Auction registration is not started yet");
		}
		if(auctionPreparation.getRegistrationEndDateTime().isBefore(currentDateTime)) {
			throw new DataMisMatchException("Auction registration is closed");
		}
		BidderAuctionEnrollment bidderAuctionEnrollment = bidderAuctionEnrollmentVO.bidderAuctionEnrollmentVOToBidderAuctionEnrollment();
		bidderAuctionEnrollment.setAuctionPreparation(auctionPreparation);
		bidderAuctionEnrollment.setUser(LoggedInUser.getLoggedInUserDetails().getUser());
	    bidderAuctionEnrollment = this.bidderAuctionEnrollmentDao.save(bidderAuctionEnrollment);	
	    bidderAuctionEnrollmentVO.setId(bidderAuctionEnrollment.getId());
	    return bidderAuctionEnrollmentVO;
	}
	
	@Override
	public List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionPreparationId);
		return this.bidderAuctionEnrollmentDao.findAllByAuctionPreparation(auctionPreparation)
				.stream().map(BidderAuctionEnrollment::bidderAuctionEnrollmentToBidderAuctionEnrollmentVO).toList();
	}

}
