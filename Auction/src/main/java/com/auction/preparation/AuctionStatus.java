package com.auction.preparation;

public enum AuctionStatus {
    DRAFT("Draft"),RETURN("Return"),APPROVE("Approve"),PUBLISH("Publish");

	private String status;
	private AuctionStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}

