package com.auction.bidding;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionItem;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.user.User;
import com.auction.user.UserVO;
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
			boolean isFinishDateTimeExtend = this.updateAuctionDetails(auctionPreparation, currentDateTime);
			//Duration duration = Duration.between(currentDateTime,auctionPreparation.getAuctionFinishTime());
			biddingVO.setRemainingTime(currentDateTime.until(auctionPreparation.getAuctionFinishTime(), ChronoUnit.MILLIS));
			//biddingVO.setRemainingTime(duration.toSeconds());
			biddingVO.setFinishTimeExtend(isFinishDateTimeExtend);	
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
		AuctionItem auctionItem = auctionPreparation.getAuctionItems().stream().findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Auction item not exist for Auction"));
		Optional<Double> optionalBidding =
				this.biddingDao.findBiddingAmountByAuctionPreparation(auctionPreparation, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")));
		Bidding bidding = new Bidding();
		bidding.setId(biddingVO.getId());
		bidding.setBiddingAt(LocalDateTime.now());
		double bidAmount = 0.0;
		if(optionalBidding.isPresent()) {
			double lastBidAmount = optionalBidding.get();
			bidAmount = lastBidAmount+(auctionItem.getModifierValue()*biddingVO.getModifierValueMultiplyBy());
		} else {
			//bidAmount = auctionPreparation.get
		}
	//	double bidAmount = au
	//	bidding.setBiddingAmount(bidAmount);
		bidding.setAuctionPreparation(auctionPreparation);
		bidding.setBidder(user);
		bidding = this.biddingDao.save(bidding);
		biddingVO.setBidder(user.userToUserVO());
		biddingVO.setId(bidding.getId());
		biddingVO.setBiddingAmount(bidAmount);
		biddingVO.setBiddingAt(bidding.getBiddingAt());
	}
	
	private boolean updateAuctionDetails(AuctionPreparation auctionPreparation, LocalDateTime currentDateTime) {
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
		       return true;
		    }
			 
		 }
		return false;
	}
	
	
	@Override
	public BiddingVO lastBidOfAuction(Long auctionId) {
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.getById(auctionId);
		return this.lastBidOfAuctionForBidder(auctionPreparation);
	}
	
	@Override
	public BiddingVO lastBidOfAuctionForBidder(AuctionPreparation auctionPreparation) {
		Optional<Bidding> optionalBidding = this.biddingDao.findByAuctionPreparation(auctionPreparation, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")));
		if(optionalBidding.isPresent()) {
			Bidding bidding = optionalBidding.get();
			BiddingVO biddingVO = bidding.biddingToBiddingVO();
			UserVO userVO = new UserVO();
			userVO.setId(bidding.getBidder().getId());
			biddingVO.setBidder(userVO);
			biddingVO.setRemainingTime(LocalDateTime.now().until(auctionPreparation.getAuctionFinishTime(), ChronoUnit.MILLIS));
			biddingVO.setFinishTimeExtend(!auctionPreparation.getAuctionFinishTime().isEqual(auctionPreparation.getAuctionEndDateTime()));
			biddingVO.setAuctionPreparation(auctionPreparation.auctionPreparationToAuctionPreparationVO());
		    return biddingVO;	
		}
		BiddingVO biddingVO = new BiddingVO();
		biddingVO.setRemainingTime(LocalDateTime.now().until(auctionPreparation.getAuctionFinishTime(), ChronoUnit.MILLIS));
		biddingVO.setFinishTimeExtend(!auctionPreparation.getAuctionFinishTime().isEqual(auctionPreparation.getAuctionEndDateTime()));
		biddingVO.setBiddingAmount(0);
		biddingVO.setAuctionPreparation(auctionPreparation.auctionPreparationToAuctionPreparationVO());
		biddingVO.setBidder(new UserVO());
	   return biddingVO;
	}

}
