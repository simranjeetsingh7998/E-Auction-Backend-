package com.auction.bidding;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.bidder.enrollment.IBidderAuctionEnrollmentDao;
import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceAlreadyExist;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionItem;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionPreparationSpecification;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.preparation.IPropertiesDao;
import com.auction.properties.PropertiesStatus;
import com.auction.user.User;
import com.auction.user.UserVO;
import com.auction.util.LoggedInUser;

@Service
public class BiddingService implements IBiddingService {
	
	@Autowired
	private IBiddingDao biddingDao;
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Autowired 
	private IPropertiesDao propertiesDao;
	
	@Autowired
	private IBidderAuctionEnrollmentDao bidderAuctionEnrollmentDao;
	
	@Transactional
	@Override
	public BiddingVO bidding(BiddingVO biddingVO) {
		List<AuctionPreparation> auctionPreparationList = this.auctionPreparationDao.findAll(AuctionPreparationSpecification
				.findAuctionWithAuctionItemsAndOrganizationItemById(biddingVO.getAuctionPreparation().getId()));
		if(auctionPreparationList.isEmpty())
			throw new ResourceNotFoundException("Auction not found");
		AuctionPreparation auctionPreparation = auctionPreparationList.get(0);
		if(!this.isUserValidForRound(auctionPreparation)) {
			throw new DataMisMatchException("You can't bid. Maybe your EmdLimit reached");
		}
		long roundNumber = this.getRoundNumberAndUpdateAuctionStartAndFinishDateTime(auctionPreparation);
		biddingVO.setRound(""+roundNumber);
		LocalDateTime currentDateTime = LocalDateTime.now();
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())
		   && currentDateTime.isAfter(auctionPreparation.getAuctionStartDateTime())	 
		   && currentDateTime.isBefore(auctionPreparation.getAuctionFinishTime())) {
			this.bid(biddingVO, auctionPreparation);
			boolean isFinishDateTimeExtend = this.updateAuctionDetails(auctionPreparation, currentDateTime);
			biddingVO.setRemainingTime(currentDateTime.until(auctionPreparation.getAuctionFinishTime(), ChronoUnit.MILLIS));
			biddingVO.setFinishTimeExtend(isFinishDateTimeExtend);	
		  return biddingVO;
		}
		else if(!auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())) {
			throw new DataMisMatchException("Auction is not scheduled yet");
		}
		else if(!currentDateTime.isAfter(auctionPreparation.getAuctionStartDateTime())) {
			throw new DataMisMatchException("Round is not started yet");
		}
		else if(currentDateTime.isAfter(auctionPreparation.getAuctionFinishTime())) {
			throw new DataMisMatchException("Round is closed");
		}
		return null;	
	}
	
	
	private boolean isUserValidForRound(AuctionPreparation auctionPreparation) {
		User bidder = LoggedInUser.getLoggedInUserDetails().getUser();
		Integer userRoundsCount = this.bidderAuctionEnrollmentDao.findRoundParticipantCount(auctionPreparation, bidder);
	    if(Objects.isNull(userRoundsCount))
	    	  return false;
	    long h1Count = this.biddingDao.countByAuctionPreparationAndBidderAndRoundClosedAtIsNotNull(auctionPreparation, bidder); 
		return h1Count < userRoundsCount;
	}
	
	private long getRoundNumberAndUpdateAuctionStartAndFinishDateTime(AuctionPreparation auctionPreparation) throws ResourceNotFoundException {
	    long roundNumber = this.findRoundNumber(auctionPreparation);
	    if(roundNumber>0) {
	    	LocalDateTime auctionStartDateTime = auctionPreparation.getAuctionStartDateTime();
	    	LocalDateTime auctionEndDateTime = auctionPreparation.getAuctionEndDateTime();
	    	LocalDateTime auctionFinishDateTime = auctionPreparation.getAuctionFinishTime();
	    	// if auction end and finish date time is same
	    	long minutesDuration = auctionStartDateTime.until(auctionEndDateTime, ChronoUnit.MINUTES)*roundNumber;
	    	// if auction finish date time is greater than end date time adding difference in minuteDuration
	    	if(auctionFinishDateTime.isAfter(auctionEndDateTime))
	    		minutesDuration += auctionEndDateTime.until(auctionFinishDateTime, ChronoUnit.MINUTES);
	    	
	    	// update auction start date time 
	    	auctionPreparation.setAuctionStartDateTime(auctionStartDateTime
	    			.plusMinutes(!Objects.isNull(auctionPreparation.getIntervalInMinutes()) ? 
	    					auctionPreparation.getIntervalInMinutes()+(minutesDuration) : 0));
	    	
	    	// update auction finish date time
	    	auctionPreparation.setAuctionFinishTime(auctionFinishDateTime.plusMinutes(minutesDuration));
	    }
	   return roundNumber+1; 
	}
	
	
	
	private long findRoundNumber(AuctionPreparation auctionPreparation) {
		AuctionItem auctionItem = auctionPreparation.getAuctionItems().stream().findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Auction item not exist for Auction"));
	//	System.out.println(auctionItem.getOrganizationItem().getId());
		long unsoldPropertiesCount = this.propertiesDao.countByAuctionPreparationAndAuctionItemProprties_OrganizationItemAndAuctionItemProprties_PropertiesStatusAndAuctionItemProprties_IsActiveTrue(
				auctionPreparation, auctionItem.getOrganizationItem(), PropertiesStatus.UNSOLD);
		if(unsoldPropertiesCount == 0)
			  throw new ResourceNotFoundException("All rounds are completed");
		long totalProperties = this.propertiesDao.countByAuctionPreparationAndAuctionItemProprties_OrganizationItemAndAuctionItemProprties_IsActiveTrue(
				auctionPreparation, auctionItem.getOrganizationItem());
		return totalProperties-unsoldPropertiesCount;
	}
	
	public Bidding findLastBidOfAuction(AuctionPreparation auctionPreparation) {
		Bidding bidding = this.biddingDao.findByAuctionPreparation(auctionPreparation, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")))
				.orElseThrow(() -> new ResourceNotFoundException("Auction can't be closed because no bid found for auction"));
		if(bidding.getRoundClosedAt() != null)
			  throw new ResourceAlreadyExist("Round already closed");
		bidding.setRoundStartAt(auctionPreparation.getAuctionStartDateTime());
		bidding.setRoundClosedAt(auctionPreparation.getAuctionFinishTime());
		bidding.setAuctionPreparation(auctionPreparation);
	   return bidding;	
	}
	
	private void bid(BiddingVO biddingVO, AuctionPreparation auctionPreparation) {
		User user = LoggedInUser.getLoggedInUserDetails().getUser();
		AuctionItem auctionItem = auctionPreparation.getAuctionItems().stream().findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Auction item not exist for Auction"));
		Optional<Double> optionalBidding =
				this.biddingDao.findBiddingAmountByAuctionPreparationAndRoundNo(auctionPreparation,biddingVO.getRound(), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")));
		Bidding bidding = new Bidding();
		bidding.setId(biddingVO.getId());
		bidding.setBiddingAt(LocalDateTime.now());
		double bidAmount = auctionItem.getModifierValue()*biddingVO.getModifierValueMultiplyBy();
		if(optionalBidding.isPresent()) {
			double lastBidAmount = optionalBidding.get();
			bidAmount +=lastBidAmount;
		} else {
			bidAmount += auctionItem.getReservedPrice();
		}
		bidding.setBiddingAmount(bidAmount);
		bidding.setAuctionPreparation(auctionPreparation);
		bidding.setBidder(user);
		bidding.setRoundNo(biddingVO.getRound());
		bidding = this.biddingDao.save(bidding);
		biddingVO.setBidder(user.userToUserVO());
		biddingVO.setId(bidding.getId());
		biddingVO.setBiddingAmount(bidAmount);
		biddingVO.setBiddingAt(bidding.getBiddingAt());
	}
	
	private boolean updateAuctionDetails(AuctionPreparation auctionPreparation, LocalDateTime currentDateTime) {
		  long difference = Duration.between(currentDateTime,auctionPreparation.getAuctionFinishTime()).toMinutes();
		   if(difference <= auctionPreparation.getAuctionExtendTimeCondition()) {
			 boolean extend = extendCountLeft(auctionPreparation);
			 if(extend) {
		       auctionPreparation.setAuctionFinishTime(auctionPreparation.getAuctionFinishTime().plusMinutes(auctionPreparation.getAuctionExtendMinutes()));
		       this.auctionPreparationDao.save(auctionPreparation);
		       return true;
		    }
			 
		 }
		return false;
	}
	
	private boolean extendCountLeft(AuctionPreparation auctionPreparation) {
		 Integer auctionExtendLimit = auctionPreparation.getAuctionExtendLimit();
		 boolean extend = false;
		 if(!Objects.isNull(auctionExtendLimit)) {
			long finishAndEndTimeDiff =  Duration.between(auctionPreparation.getAuctionEndDateTime(),
					auctionPreparation.getAuctionFinishTime()).toMinutes();
			if(finishAndEndTimeDiff > 0) {
				extend = finishAndEndTimeDiff/auctionPreparation.getAuctionExtendMinutes() == auctionExtendLimit;
			  } 
		 }
		 return extend;
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
			biddingVO.setRound(bidding.getRoundNo());
		    return biddingVO;	
		}
		BiddingVO biddingVO = new BiddingVO();
		biddingVO.setRemainingTime(LocalDateTime.now().until(auctionPreparation.getAuctionFinishTime(), ChronoUnit.MILLIS));
		biddingVO.setFinishTimeExtend(!auctionPreparation.getAuctionFinishTime().isEqual(auctionPreparation.getAuctionEndDateTime()));
		biddingVO.setBiddingAmount(0);
		biddingVO.setAuctionPreparation(auctionPreparation.auctionPreparationToAuctionPreparationVO());
		biddingVO.setRound(""+1);
		biddingVO.setBidder(new UserVO());
	   return biddingVO;
	}
	
	@Override
	public List<BidHistory> findBidHistoryByActionPreparation(Long auctionId) {
		return this.biddingDao.findBidHistoryByAuctionPreparation(auctionId)
				.stream().map(tuple -> {
				//   Object	tuple.get("round");
					 BidHistory bidHistory = new BidHistory();
					 bidHistory.setBidder(tuple.get("firstName").toString().concat(" ").concat(tuple.get("lastName").toString()));
					 bidHistory.setAmount(Double.parseDouble(tuple.get("amount").toString()));
					 bidHistory.setRound(tuple.get("round", String.class));
					 bidHistory.setBidAt(tuple.get("bidAt").toString());
					 return bidHistory;
				}).toList();
	}
	
	@Transactional
	@Override
	public long closeRoundByAuctionPreparation(Long auctionId) {
	    AuctionPreparation auctionPreparation =  this.auctionPreparationDao.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
	    return closeRound(auctionPreparation);
	}
	
	@Override
	@Transactional
	public long closeRound(AuctionPreparation auctionPreparation) throws ResourceNotFoundException {
	    long unsoldPropertiesCount = findRoundNumber(auctionPreparation);
	    Bidding bidding = findLastBidOfAuction(auctionPreparation);
	    bidding.setRoundNo(""+unsoldPropertiesCount);
	    this.biddingDao.save(bidding);
		return (unsoldPropertiesCount);
	}

}
