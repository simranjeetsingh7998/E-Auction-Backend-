package com.auction.cron.jobs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.auction.bidding.IBiddingService;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.method.AuctionMethod;
import com.auction.method.AuctionMethodEnum;
import com.auction.method.IAuctionMethodDao;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionPreparationDao;

@Component
public class CronJobs {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CronJobs.class);
	
	@Autowired
	private IBiddingService biddingService;
	
	@Autowired 
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Autowired IAuctionMethodDao auctionMethodDao;
	
	//@Scheduled(cron = "0 * * * * *")
	private void closeRound() {
		List<AuctionPreparation> auctionPreparationList
		= this.auctionPreparationDao.findAllByAuctionStatusAndAuctionFinishTimeBefore(AuctionStatus.SCHEDULED, LocalDateTime.now());
	    auctionPreparationList.forEach(auction -> {
	    	 try {
	    	     this.biddingService.closeRound(auction);
			} catch (ResourceNotFoundException e) {
				LOGGER.error("closeRound", e);
			}
	    });
	}
	
	@Scheduled(cron = "0 * * * * *")
	private void closeNormalRounds() {
		 List<AuctionMethod> auctionMethodList = this.auctionMethodDao.findAllByMethodAndIsActiveTrue(AuctionMethodEnum.NORMAL.getMethod());
		 if(!Objects.isNull(auctionMethodList) && !auctionMethodList.isEmpty()) {
			   this.auctionPreparationDao.findAllByAuctionStatusAndAuctionMethod(AuctionStatus.SCHEDULED, auctionMethodList.get(0))
			   .forEach(auctionPreparation -> this.biddingService.closeAndConcludeAndReservePropertyForNormalAuction(auctionPreparation));
		 }
	}
}
