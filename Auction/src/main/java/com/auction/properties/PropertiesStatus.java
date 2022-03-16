package com.auction.properties;

public enum PropertiesStatus {
	
    SOLD("Sold"), UNSOLD("UNSOLD"), RESERVED("Reserved");

	private String status;
	private PropertiesStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}

}
