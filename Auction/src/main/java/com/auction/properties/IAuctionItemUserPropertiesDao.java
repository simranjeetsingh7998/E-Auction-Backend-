package com.auction.properties;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auction.user.User;

public interface IAuctionItemUserPropertiesDao  extends JpaRepository<AuctionItemUserProperties, Long> {

	long countByAuctionItemProprtiesAndBidderAndPropertiesStatus(AuctionItemProprties auctionItemProperty,User bidder,PropertiesStatus propertiesStatus);
}
