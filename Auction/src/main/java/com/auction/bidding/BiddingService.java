package com.auction.bidding;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.user.User;
import com.auction.util.LoggedInUser;

@Service
public class BiddingService implements IBiddingService {
	
	@Autowired
	private IBiddingDao biddingDao;
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Transactional
	@Override
	public BiddingVO bidding(BiddingVO biddingVO) {
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(biddingVO.getAuctionPreparation().getId())
		.orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		LocalDateTime currentDateTime = LocalDateTime.now();
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())
		   && currentDateTime.isAfter(auctionPreparation.getAuctionStartDateTime())	 
		   && currentDateTime.isBefore(auctionPreparation.getAuctionFinishTime())) {
			this.bid(biddingVO, auctionPreparation);
			this.updateAuctionDetails(auctionPreparation, currentDateTime);
			return biddingVO;
		}
		else if(!auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())){
			throw new DataMisMatchException("Auction is not scheduled yet");
		}
		else if(!currentDateTime.isAfter(auctionPreparation.getAuctionStartDateTime())) {
			throw new DataMisMatchException("Auction is not started yet");
		}
		else if(currentDateTime.isAfter(auctionPreparation.getAuctionFinishTime())) {
			throw new DataMisMatchException("Auction is closed");
		}
		return null;
		
	}
	
	private void bid(BiddingVO biddingVO, AuctionPreparation auctionPreparation) {
		User user = LoggedInUser.getLoggedInUserDetails().getUser();
		Bidding bidding = new Bidding();
		bidding.setId(biddingVO.getId());
		bidding.setBiddingAt(LocalDateTime.now());
		bidding.setBiddingAmount(biddingVO.getBiddingAmount());
		bidding.setAuctionPreparation(auctionPreparation);
		bidding.setBidder(user);
		bidding = this.biddingDao.save(bidding);
		biddingVO.setBidder(user.userToUserVO());
		biddingVO.setId(bidding.getId());
		biddingVO.setBiddingAt(bidding.getBiddingAt());
	}
	
	private void updateAuctionDetails(AuctionPreparation auctionPreparation, LocalDateTime currentDateTime) {
		  long difference = Duration.between(currentDateTime,auctionPreparation.getAuctionFinishTime()).toMinutes();
		   if(difference <= auctionPreparation.getAuctionExtendTimeCondition()) {
			 Integer auctionExtendLimit = auctionPreparation.getAuctionExtendLimit();
			 boolean extend = true;
			 if(!Objects.isNull(auctionExtendLimit)) {
				long finishAndEndTimeDiff =  Duration.between(auctionPreparation.getAuctionEndDateTime(),
						auctionPreparation.getAuctionFinishTime()).toMinutes();
				if(finishAndEndTimeDiff > 0) {
					extend = finishAndEndTimeDiff/auctionPreparation.getAuctionExtendMinutes() != auctionExtendLimit;
				  } 
			 }
			 
			 if(extend) {
		      // currentDateTime = currentDateTime.plusMinutes(auctionPreparation.getAuctionExtendMinutes());
		       auctionPreparation.setAuctionFinishTime(auctionPreparation.getAuctionFinishTime().plusMinutes(auctionPreparation.getAuctionExtendMinutes()));
		       this.auctionPreparationDao.save(auctionPreparation);
		    }
		 }
	}

}
