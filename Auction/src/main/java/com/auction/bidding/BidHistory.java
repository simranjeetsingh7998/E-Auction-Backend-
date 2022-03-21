package com.auction.bidding;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BidHistory {
	
	private String bidder;
	
	private Double amount;
	
	private String round;
	
	@JsonProperty("bid_at")
	private String bidAt;

}
