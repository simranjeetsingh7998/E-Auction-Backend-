package com.auction.bidding;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	@Override
	public BiddingVO bidding(BiddingVO biddingVO) {
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(biddingVO.getAuctionPreparation().getId())
		.orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.SCHEDULED.getStatus())) {
			
		}
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
		return biddingVO;
	}

}
