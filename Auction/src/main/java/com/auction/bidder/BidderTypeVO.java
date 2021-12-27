package com.auction.bidder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class BidderTypeVO {
	
	private int id;
	
	@Schema(defaultValue = "Indivisiual", description = "enter type of bidder")
	private String bType;
	
	@Schema(defaultValue = "true", description = "enter status of bidder type")
	private boolean isActive;
	
	public BidderType bidderTypeVOToBidderType() {
		 BidderType bidderType = new BidderType();
		 bidderType.setId(id);
		 bidderType.setBType(bType);
		 bidderType.setActive(isActive);
		return bidderType; 
	}
	

}
