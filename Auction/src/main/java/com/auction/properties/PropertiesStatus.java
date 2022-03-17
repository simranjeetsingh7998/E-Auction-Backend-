package com.auction.properties;

public enum PropertiesStatus {
	
    SOLD("SOLD"), UNSOLD("UNSOLD"), RESERVED("RESERVED");

	private String status;
	private PropertiesStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}

}
