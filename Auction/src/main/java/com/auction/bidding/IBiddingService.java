package com.auction.bidding;

import java.util.List;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;

public interface IBiddingService {

	BiddingVO bidding(BiddingVO biddingVO);
	
	BiddingVO lastBidOfAuction(Long auctionId);

	BiddingVO lastBidOfAuctionForBidder(AuctionPreparation auctionPreparation);
	
	long closeRoundByAuctionPreparation(Long auctionId);

	long closeRound(AuctionPreparation auctionPreparation) throws ResourceNotFoundException;
	
	List<BidHistory> findBidHistoryByActionPreparation(Long auctionId);
	
}
