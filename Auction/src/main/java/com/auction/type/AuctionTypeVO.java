package com.auction.type;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuctionTypeVO {
	
	private Integer id;
	@Schema(defaultValue = "Auction Type Name", description = "enter name of auction type")
	@NotBlank(message = "{auctionType.required}")
	@JsonProperty("auction_type")
	private String aType;
	@JsonProperty("active")
	private boolean isActive;
	
	public AuctionType auctionTypeVOToAuctionType() {
		 AuctionType auctionType = new AuctionType();
		 auctionType.setId(id);
		 auctionType.setAType(aType);
		 auctionType.setActive(isActive);
	   return auctionType;
	}

}
