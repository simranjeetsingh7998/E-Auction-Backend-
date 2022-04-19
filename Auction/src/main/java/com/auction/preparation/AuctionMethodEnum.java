package com.auction.preparation;

public enum AuctionMethodEnum {
	
	NORMAL("Normal"), ROUNDWISE("Roundwise"),H1BiddingRule("H1 Bidding Rule") ;

	private String method;
	private AuctionMethodEnum(String method) {
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}

}
