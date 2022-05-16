package com.auction.bidding;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.bidder.enrollment.IBidderAuctionEnrollmentDao;
import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceAlreadyExist;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.method.IAuctionMethodDao;
import com.auction.preparation.AuctionExtendedHistory;
import com.auction.preparation.AuctionItem;
import com.auction.preparation.AuctionMethodEnum;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionPreparationSpecification;
import com.auction.preparation.AuctionPreparationVO;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionExtendedHistoryDao;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.preparation.IPropertiesDao;
import com.auction.preparation.Properties;
import com.auction.properties.AuctionItemProprties;
import com.auction.properties.AuctionItemProprtiesVO;
import com.auction.properties.AuctionItemUserProperties;
import com.auction.properties.IAuctionItemProprtiesDao;
import com.auction.properties.IAuctionItemUserPropertiesDao;
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
	
	@Autowired
	private IAuctionItemUserPropertiesDao auctionItemUserCountDao;
	
	@Autowired 
	private IAuctionItemProprtiesDao auctionItemPropertiesDao;
	
	@Autowired
	private IAuctionExtendedHistoryDao auctionExtendedHistoryDao;
	
	@Autowired
	private IAuctionMethodDao auctionMethodDao;
	
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
		String auctionMethod = this.auctionMethodDao.findAllByAuctionPreparations(auctionPreparation).get(0).getMethod();
		if(auctionMethod.equals(com.auction.method.AuctionMethodEnum.ROUNDWISE.getMethod())) {
			return this.roundWiseBidding(biddingVO, auctionPreparation);
		} else if(auctionMethod.equals(com.auction.method.AuctionMethodEnum.NORMAL.getMethod())) {
			return this.normalAuctionBid(biddingVO, auctionPreparation);
		}
		return null;
	
	}
	
	
	private BiddingVO roundWiseBidding(BiddingVO biddingVO, AuctionPreparation auctionPreparation) {
		long roundNumber = this.findRoundNumber(auctionPreparation);
		int round = Math.toIntExact(roundNumber);
		
		biddingVO.setRound(""+(round+1));
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		Map<String, LocalDateTime> startEndDateTime = this.getAuctionStartAndEndDateTime(auctionPreparation, round+1);
		LocalDateTime auctionStartDateTime = startEndDateTime.get("start");
		LocalDateTime auctionFinishDateTime = startEndDateTime.get("end");
	    
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())
		   && currentDateTime.isAfter(auctionStartDateTime)	 
		   && currentDateTime.isBefore(auctionFinishDateTime)
		   ) {
			this.bid(biddingVO, auctionPreparation);
			// adding auction extend history
			boolean isFinishDateTimeExtend = this.updateAuctionDetails(auctionPreparation, currentDateTime, auctionFinishDateTime);
			long difference = 0;
			 long timeExtendCount = this.getAuctionExtendCount(auctionPreparation.getId(), round+1);
			if(isFinishDateTimeExtend && auctionPreparation.getAuctionExtendLimit() > 0) {
			     if(timeExtendCount < auctionPreparation.getAuctionExtendLimit()) {
				    this.addAuctionRoundExtended(round+1, auctionPreparation);
				    difference = difference+(auctionPreparation.getAuctionExtendMinutes()*(60*1000));
				    biddingVO.setTimeExtendCount(timeExtendCount+1);
			     }
			}else {
				 biddingVO.setTimeExtendCount(timeExtendCount);
			}
			biddingVO.setRemainingTime(LocalDateTime.now().until(auctionFinishDateTime, ChronoUnit.MILLIS)+difference);
			biddingVO.setFinishTimeExtend(isFinishDateTimeExtend);
		  return biddingVO;
		}
		else if(!auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())) {
			throw new DataMisMatchException("Auction is not scheduled yet");
		}
		else if(!currentDateTime.isAfter(auctionPreparation.getAuctionStartDateTime())) {
			throw new DataMisMatchException("Round is not started yet");
		}
		else if(currentDateTime.isAfter(auctionFinishDateTime)) {
			if(this.biddingDao.countByAuctionPreparationAndRoundNo(auctionPreparation, String.valueOf(round+1)) == 0) {
				this.concludedAuction(auctionPreparation);
			}
			throw new DataMisMatchException("Round is closed");
		}
		return null;
	}
	
	private BiddingVO normalAuctionBid(BiddingVO biddingVO, AuctionPreparation auctionPreparation) {
		int round = 0;
		
		biddingVO.setRound(""+(round+1));
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		Map<String, LocalDateTime> startEndDateTime = this.getAuctionStartAndEndDateTime(auctionPreparation, round+1);
		LocalDateTime auctionStartDateTime = startEndDateTime.get("start");
		LocalDateTime auctionFinishDateTime = startEndDateTime.get("end");
	    
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())
		   && currentDateTime.isAfter(auctionStartDateTime)	 
		   && currentDateTime.isBefore(auctionFinishDateTime)
		   ) {
			this.bid(biddingVO, auctionPreparation);
			// adding auction extend history
			boolean isFinishDateTimeExtend = this.updateAuctionDetails(auctionPreparation, currentDateTime, auctionFinishDateTime);
			long difference = 0;
			 long timeExtendCount = this.getAuctionExtendCount(auctionPreparation.getId(), round+1);
			if(isFinishDateTimeExtend && auctionPreparation.getAuctionExtendLimit() > 0) {
			     if(timeExtendCount < auctionPreparation.getAuctionExtendLimit()) {
				    this.addAuctionRoundExtended(round+1, auctionPreparation);
				    difference = difference+(auctionPreparation.getAuctionExtendMinutes()*(60*1000));
				    biddingVO.setTimeExtendCount(timeExtendCount+1);
			     }
			}else {
				 biddingVO.setTimeExtendCount(timeExtendCount);
			}
			biddingVO.setRemainingTime(LocalDateTime.now().until(auctionFinishDateTime, ChronoUnit.MILLIS)+difference);
			biddingVO.setFinishTimeExtend(isFinishDateTimeExtend);
		  return biddingVO;
		}
		else if(!auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())) {
			throw new DataMisMatchException("Auction is not scheduled yet");
		}
		else if(!currentDateTime.isAfter(auctionPreparation.getAuctionStartDateTime())) {
			throw new DataMisMatchException("Round is not started yet");
		}
		else if(currentDateTime.isAfter(auctionFinishDateTime)) {
			if(this.biddingDao.countByAuctionPreparationAndRoundNo(auctionPreparation, String.valueOf(round+1)) == 0) {
				this.concludedAuction(auctionPreparation);
			}
			throw new DataMisMatchException("Round is closed");
		}
		return null;
	}
	
	private void concludedAuction(AuctionPreparation auctionPreparation) {
		  auctionPreparation.setAuctionStatus(AuctionStatus.CONCLUDED);
		  this.auctionPreparationDao.save(auctionPreparation);
		//  throw new ResourceNotFoundException("All rounds are completed");
	}
	
	private long getAuctionExtendCount(Long auctionPreparationId, int round) {
		return this.auctionExtendedHistoryDao.countByAuctionPreparationAndRound(auctionPreparationId, round);
	}
	
	private void addAuctionRoundExtended(int round, AuctionPreparation auctionPreparation) {
		Integer extendCount = this.auctionExtendedHistoryDao.findByAuctionPreparationAndRound(auctionPreparation.getId(), round,
				PageRequest.of(0, 1, Sort.by(Direction.DESC, "id")));
		if(Objects.isNull(extendCount))
			  extendCount = 0;
		AuctionExtendedHistory auctionExtendedHistory = new AuctionExtendedHistory();
		auctionExtendedHistory.setRound(round);
		auctionExtendedHistory.setAuctionPreparation(auctionPreparation.getId());
		auctionExtendedHistory.setExtendCount(extendCount+1);
	//	auctionExtendedHistory.setPreparation(auctionPreparation);
		this.auctionExtendedHistoryDao.save(auctionExtendedHistory);
	}
	
	
	private boolean isUserValidForRound(AuctionPreparation auctionPreparation) {
		User bidder = LoggedInUser.getLoggedInUserDetails().getUser();
		Integer userRoundsCount = this.bidderAuctionEnrollmentDao.findRoundParticipantCount(auctionPreparation, bidder);
	    if(Objects.isNull(userRoundsCount))
	    	  return false;
	    long h1Count = this.biddingDao.countByAuctionPreparationAndBidderAndRoundClosedAtIsNotNull(auctionPreparation, bidder); 
		return h1Count < userRoundsCount;
	}
	
	
	
	private long findRoundNumber(AuctionPreparation auctionPreparation) {
		AuctionItem auctionItem = auctionPreparation.getAuctionItems().stream().findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Auction item not exist for Auction"));
		long unsoldPropertiesCount = this.propertiesDao.countByAuctionPreparationAndAuctionItemProprties_OrganizationItemAndAuctionItemProprties_PropertiesStatusAndAuctionItemProprties_IsActiveTrue(
				auctionPreparation, auctionItem.getOrganizationItem(), PropertiesStatus.UNSOLD);
		if(unsoldPropertiesCount == 0) {
			  auctionPreparation.setAuctionStatus(AuctionStatus.CONCLUDED);
			  this.auctionPreparationDao.save(auctionPreparation);
			  throw new ResourceNotFoundException("All rounds are completed");
		}
		long totalProperties = this.propertiesDao.countByAuctionPreparationAndAuctionItemProprties_OrganizationItemAndAuctionItemProprties_IsActiveTrue(
				auctionPreparation, auctionItem.getOrganizationItem());
		return totalProperties-unsoldPropertiesCount;
	}
	
	public Bidding findLastBidOfAuction(AuctionPreparation auctionPreparation) {
//		Bidding bidding = this.biddingDao.findByAuctionPreparation(auctionPreparation, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")))
//				.orElseThrow(() -> new ResourceNotFoundException("Auction can't be closed because no bid found for auction"));
		Optional<Bidding> optBidding = this.biddingDao.findByAuctionPreparation(auctionPreparation, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")));
		if(!optBidding.isPresent()) {
			return null;
		}
		Bidding bidding = optBidding.get();
		if(bidding.getRoundClosedAt() != null)
			return null;
			  //throw new ResourceAlreadyExist("Round already closed");
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
				this.biddingDao.findBiddingAmountByAuctionPreparationAndRoundNo(auctionPreparation,biddingVO.getRound(), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"id")));
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
		if(!bidding.getRoundNo().equals("0"))
		  bidding = this.biddingDao.save(bidding);
		biddingVO.setBidder(user.userToUserVO());
		biddingVO.setId(0L);
		biddingVO.setBiddingAmount(bidAmount);
		biddingVO.setBiddingAt(bidding.getBiddingAt());
	}
	
	private boolean updateAuctionDetails(AuctionPreparation auctionPreparation, LocalDateTime currentDateTime, LocalDateTime auctionFinishDateTime) {
		  long difference = Duration.between(currentDateTime,auctionFinishDateTime).toSeconds();
		  return difference <= (auctionPreparation.getAuctionExtendTimeCondition()*60);
	}
	
	
	@Override
	public BiddingVO lastBidOfAuction(Long auctionId) {
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.getById(auctionId);
		return this.lastBidOfAuctionForBidder(auctionPreparation);
	}
	
	@Override
	public BiddingVO lastBidOfAuctionForBidder(AuctionPreparation auctionPreparation) {
		Optional<Bidding> optionalBidding = this.biddingDao.findByAuctionPreparation(auctionPreparation, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,"biddingAt")));
		AuctionPreparationVO  auctionPreparationVO = auctionPreparation.auctionPreparationToAuctionPreparationVO();
		auctionPreparationVO.setAuctionItems(auctionPreparation.getAuctionItems().stream().map(AuctionItem::auctionItemToAuctionItemVO).toList());
		auctionPreparationVO.setAuctionScheduleVO(auctionPreparation.getAuctionScheduleVO());
		int roundNumber = 0;
		long remainingTime = 0;
		long roundStartRemainingTime = auctionPreparation.getIntervalInMinutes()*60000;
		UserVO userVO = new UserVO();
		if(optionalBidding.isPresent() && Objects.isNull(optionalBidding.get().getRoundClosedAt())) {
			Bidding bidding = optionalBidding.get();
			roundNumber = Integer.parseInt(bidding.getRoundNo());
			BiddingVO biddingVO = bidding.biddingToBiddingVO();
			userVO.setId(bidding.getBidder().getId());
			biddingVO.setBidder(userVO);
			 remainingTime = this.getAuctionRemainingTime(auctionPreparation, roundNumber);
			// roundStartRemainingTime = auctionPreparation.getIntervalInMinutes()*60*1000;
			if(remainingTime < 0) {
				roundStartRemainingTime = roundStartRemainingTime - (-remainingTime);
				remainingTime = 0;
			}
			biddingVO.setRemainingTime(remainingTime);
			biddingVO.setRoundStartRemainingTime(roundStartRemainingTime);
			biddingVO.setFinishTimeExtend(!auctionPreparation.getAuctionFinishTime().isEqual(auctionPreparation.getAuctionEndDateTime()));
			biddingVO.setAuctionPreparation(auctionPreparationVO);
			biddingVO.setRound(bidding.getRoundNo());
			biddingVO.setTimeExtendCount(this.getAuctionExtendCount(auctionPreparation.getId(), roundNumber));
		    return biddingVO;
		}
		else if(optionalBidding.isPresent() && !Objects.isNull(optionalBidding.get().getRoundClosedAt())) {
			   roundNumber = Integer.parseInt(optionalBidding.get().getRoundNo());
			   remainingTime = this.getAuctionRemainingTime(auctionPreparation, roundNumber);
			   userVO.setId(optionalBidding.get().getBidder().getId());
				if(remainingTime < 0) {
					//System.out.println("Round no : "+roundNumber);
					roundStartRemainingTime = roundStartRemainingTime - (-remainingTime);
					if(roundStartRemainingTime <=0) {
						 roundNumber =roundNumber+1;
						 remainingTime = this.getAuctionRemainingTime(auctionPreparation, roundNumber);	 
					}
					else
					    remainingTime = 0;
				}
		} else {
			 //  System.out.println("Hellloooooooooooooooo");
			   roundNumber=+1;
			   remainingTime = this.getAuctionRemainingTime(auctionPreparation, roundNumber);
			   if(remainingTime < 0) {
				   roundStartRemainingTime = roundStartRemainingTime - (-remainingTime);
				     remainingTime = 0;
			   }
		}
		if(remainingTime < (-5000) && (roundStartRemainingTime < (-5000)))
			   this.concludedAuction(auctionPreparation);
		BiddingVO biddingVO = new BiddingVO();
		biddingVO.setRemainingTime(remainingTime);
		biddingVO.setRoundStartRemainingTime(roundStartRemainingTime);
		biddingVO.setFinishTimeExtend(!auctionPreparation.getAuctionFinishTime().isEqual(auctionPreparation.getAuctionEndDateTime()));
		biddingVO.setBiddingAmount(0);
		biddingVO.setAuctionPreparation(auctionPreparationVO);
		biddingVO.setRound(""+roundNumber);
		biddingVO.setTimeExtendCount(0L);
		biddingVO.setBidder(userVO);
	   return biddingVO;
	}
	
	private Map<String, LocalDateTime> getAuctionStartAndEndDateTime(AuctionPreparation auctionPreparation, int round){
		System.out.println("Round  "+round);
		Map<String, LocalDateTime> startEndDateTimeMap = new HashMap<>();
		LocalDateTime auctionStartDateTime = auctionPreparation.getAuctionStartDateTime();
    	LocalDateTime auctionEndDateTime = auctionPreparation.getAuctionEndDateTime();
    	LocalDateTime auctionFinishDateTime = auctionPreparation.getAuctionFinishTime();
    	// if auction end and finish date time is same
    	long minutesDuration = auctionStartDateTime.until(auctionEndDateTime, ChronoUnit.MINUTES)*(round-1);
//    	// if auction finish date time is greater than end date time adding difference in minuteDuration
    	Integer interValInMinutes = !Objects.isNull(auctionPreparation.getIntervalInMinutes())
		? auctionPreparation.getIntervalInMinutes() : 0;
    	Integer auctionExtendMinutes = auctionPreparation.getAuctionExtendMinutes();

    	System.out.println("Auction Start Date Time :  "+auctionStartDateTime);
    	System.out.println("Auction End Date Time :  "+auctionEndDateTime);
    	System.out.println("Auction Finish Date Time :  "+auctionFinishDateTime);
    	System.out.println("Auction Interval in Minutes :  "+auctionPreparation.getIntervalInMinutes());
    	System.out.println("Minute Duration :  "+minutesDuration);
        Integer extendCount = this.getAuctionExtend(auctionPreparation.getId(), round);
        System.out.println("Extend Count "+ extendCount);
        Integer extendForRoundGreaterThanOne = (extendCount > 0 ? extendCount : 1);
        System.out.println("Auction Extenf For Round Greater than one  "+extendForRoundGreaterThanOne);
    	//auctionStartDateTime = auctionStartDateTime.plusMinutes(
    	//		((interValInMinutes*(round-1))+minutesDuration)+ (auctionExtendMinutes *(round>1 ? (extendCount > 0 ? extendCount : 1) : extendCount)));
    	System.out.println("Auction Start Date Time :  "+auctionStartDateTime);
    	
    	// update auction finish date time
    	auctionFinishDateTime = auctionFinishDateTime.plusMinutes(
    			((interValInMinutes*(round-1))+minutesDuration)+ (auctionExtendMinutes *(round>1 ? extendForRoundGreaterThanOne  : extendCount)));
    	System.out.println("Auction Finish Date Time :  "+auctionFinishDateTime);
    	startEndDateTimeMap.put("start", auctionStartDateTime);
    	startEndDateTimeMap.put("end", auctionFinishDateTime);
    	return startEndDateTimeMap;
	}
	
	private long getAuctionRemainingTime(AuctionPreparation auctionPreparation, int round) {
		System.out.println(round);
    	Map<String, LocalDateTime> startEndDateTimeMap = this.getAuctionStartAndEndDateTime(auctionPreparation, round);
		return LocalDateTime.now().until(startEndDateTimeMap.get("end"), ChronoUnit.MILLIS);
	}
	
	private Integer getAuctionExtend(Long auctionPreparationId, int round) {
		    Integer extendCount = 0;
		    while(round > 0) {
		       Integer count =	this.auctionExtendedHistoryDao.findByAuctionPreparationAndRound(auctionPreparationId, round,
						PageRequest.of(0, 1, Sort.by(Direction.DESC, "id")));
		       if(!Objects.isNull(count))
		    	     extendCount = extendCount+count;
		       round--;
		    }
		    
		  return extendCount;  
	}
	
	@Override
	public List<BidHistory> findBidHistoryByActionPreparation(Long auctionId) {
		return this.biddingDao.findBidHistoryByAuctionPreparation(auctionId, LoggedInUser.getLoggedInUserDetails().getId())
				.stream().map(tuple -> {
					 BidHistory bidHistory = new BidHistory();
					 bidHistory.setBidder(tuple.get("firstName").toString().concat(" ").concat(tuple.get("lastName").toString()));
					 bidHistory.setAmount(Double.parseDouble(tuple.get("amount").toString()));
					 bidHistory.setRound(tuple.get("round", String.class));
					 bidHistory.setBidAt(tuple.get("bidAt").toString());
					 return bidHistory;
				}).toList();
	}
	
	@Override
	public List<BidHistory> findBidHistoryByActionPreparationForAdmin(Long auctionId) {
		return this.biddingDao.findBidHistoryByAuctionPreparationForAdmin(auctionId)
				.stream().map(tuple -> {
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
	    // closing round for normal auction method
	    if(isAuctionNormal(auctionPreparation)) 
	    	return closeAndConcludeAndReservePropertyForNormalAuction(auctionPreparation);
	    // closing round for round wise auction method
	    long unsoldPropertiesCount = closeRound(auctionPreparation);
	    if(unsoldPropertiesCount == -1)
	    	throw new ResourceAlreadyExist("Round already closed");
	    return unsoldPropertiesCount;
	}
	
	private boolean isAuctionNormal(AuctionPreparation auctionPreparation) {
		return this.auctionMethodDao.findAllByAuctionPreparations(auctionPreparation).get(0).getMethod().equals(AuctionMethodEnum.NORMAL.getMethod());	
	}
	
	@Override
	public long closeAndConcludeAndReservePropertyForNormalAuction(AuctionPreparation auctionPreparation) {
	    long remainingTime = this.getAuctionRemainingTime(auctionPreparation,1);
	    if(remainingTime > 0)
	    	return -1;
	    Bidding bidding = this.findLastBidOfAuction(auctionPreparation);
	    if(!Objects.isNull(bidding)) {
	       this.biddingDao.save(bidding); // round is closed
	    }
        this.concludedAuction(auctionPreparation);  // concluded the auction
	    return 1;
	}
	
	@Override
	@Transactional
	public long closeRound(AuctionPreparation auctionPreparation) throws ResourceNotFoundException {
	    long unsoldPropertiesCount = findRoundNumber(auctionPreparation);
	    Bidding bidding = findLastBidOfAuction(auctionPreparation);
	    if(Objects.isNull(bidding))
	    	 return -1;
	   // bidding.setRoundNo(""+(unsoldPropertiesCount+1));
	    this.biddingDao.save(bidding);
		return (unsoldPropertiesCount);
	}


	@Override
	public List<AuctionItemProprtiesVO> findUnsoldPropertiesForH1Bidder(Long auctionId) {
		AuctionPreparation auctionPreparation =  this.auctionPreparationDao.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		List<Properties> auctionProperties=propertiesDao.findAllByAuctionPreparationAndAuctionItemProprties_IsActiveTrue(auctionPreparation);
		return auctionProperties.stream().map(Properties::auctionItemProprtiesToAuctionItemProprtiesVO).toList();
	}


	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void markPropertyAsReserved(Long auctionId, Long propertyId) {
		AuctionPreparation auctionPreparation =  this.auctionPreparationDao.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		Optional<AuctionItemProprties> auctionProperty=this.auctionItemPropertiesDao.findById(propertyId);
		if(!auctionProperty.isPresent())
			 throw new ResourceNotFoundException("Property not found");
		User bidder = LoggedInUser.getLoggedInUserDetails().getUser();
		AuctionItemProprties auctionItemProprties = auctionProperty.get();
		long H1BidderCount=biddingDao.countByAuctionPreparationAndBidderAndRoundClosedAtIsNotNull(auctionPreparation, bidder);
		long reserveCountForH1Bidder =this.auctionItemUserCountDao.countByAuctionItemPropertiesAndBidderAndPropertiesStatus(
				auctionItemProprties, bidder, PropertiesStatus.RESERVED);
		
		if(H1BidderCount<=0) {
			throw new DataMisMatchException("You are not a highest bidder for the current auction");
		}else if(H1BidderCount<=reserveCountForH1Bidder) {
			throw new DataMisMatchException("No other property pending to allocate");
		}else {
			auctionItemProprties.setPropertiesStatus(PropertiesStatus.RESERVED);
			auctionItemPropertiesDao.save(auctionItemProprties);
			
			AuctionItemUserProperties userItemProperty=new AuctionItemUserProperties();
			userItemProperty.setAuctionItemProperties(auctionItemProprties);
			userItemProperty.setBidder(bidder);
			userItemProperty.setPropertiesStatus(PropertiesStatus.RESERVED);
			auctionItemUserCountDao.save(userItemProperty);
		}
		
	}

}
