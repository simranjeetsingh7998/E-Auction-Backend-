package com.auction.bidding;

import com.auction.preparation.AuctionPreparation;

public interface IBiddingService {

	BiddingVO bidding(BiddingVO biddingVO);
	
	BiddingVO lastBidOfAuction(Long auctionId);

	BiddingVO lastBidOfAuctionForBidder(AuctionPreparation auctionPreparation);
	
	long closeRoundByAuctionPreparation(Long auctionId);
	
}
