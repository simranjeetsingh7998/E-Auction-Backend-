package com.auction.method;

public enum AuctionMethodEnum {

	NORMAL("Normal"), ROUNDWISE("Roundwise"),HIBIDDINGRULE("H1 Bidding Rule");
	private String method;
	private AuctionMethodEnum(String method) {
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}
}
