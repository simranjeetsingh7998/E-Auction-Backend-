package com.auction.preparation;

public enum AuctionStatus {
    DRAFT("Draft"),RETURN("Return"),APPROVE("Approve"),PUBLISH("Publish"),SCHEDULED("Scheduled");

	private String status;
	private AuctionStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}

