package com.auction.bidding;

import java.util.List;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;
import com.auction.properties.AuctionItemProprtiesVO;

public interface IBiddingService {

	BiddingVO bidding(BiddingVO biddingVO);
	
	BiddingVO lastBidOfAuction(Long auctionId);

	BiddingVO lastBidOfAuctionForBidder(AuctionPreparation auctionPreparation);
	
	long closeRoundByAuctionPreparation(Long auctionId);

	long closeRound(AuctionPreparation auctionPreparation) throws ResourceNotFoundException;
	
	List<BidHistory> findBidHistoryByActionPreparation(Long auctionId);

	List<AuctionItemProprtiesVO> findUnsoldPropertiesForH1Bidder(Long auctionId);

	void markPropertyAsReserved(Long auctionId, Long propertyId);
	
	long closeAndConcludeAndReservePropertyForNormalAuction(AuctionPreparation auctionPreparation);
	
}
