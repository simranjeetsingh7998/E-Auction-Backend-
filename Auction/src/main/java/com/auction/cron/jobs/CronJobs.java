package com.auction.cron.jobs;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.auction.bidding.IBiddingService;
import com.auction.global.exception.ResourceNotFoundException;
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
	
	/*
	 * @Autowired private IBidderAuctionEnrollmentDao bidderAuctionEnrollmentDao;
	 */
	
	@Scheduled(cron = "0 * * * * *")
	private void closeRound() {
		List<AuctionPreparation> auctionPreparationList
		= this.auctionPreparationDao.findAllByAuctionStatusAndAuctionFinishTimeBefore(AuctionStatus.SCHEDULED, LocalDateTime.now());
	    auctionPreparationList.forEach(auction -> {
	    	 try {
	    	     this.biddingService.closeRound(auction);
			} catch (ResourceNotFoundException e) {
				/*
				 * System.out.println("Message                    "+e.getMessage());
				 * if(!e.getMessage().contains("Round already closed")) {
				 * LOGGER.error("Auction id  " +auction.getId(), e); } else {
				 * LOGGER.error("Auction id "+auction.getId()+"  "+e.getMessage()); }
				 */
				
			}
	    });
	}
	
	
	/*
	 * @Scheduled(cron = "0 * * * * *") private void sendLiveAuctionToParticipants()
	 * {
	 * 
	 * }
	 */

}
