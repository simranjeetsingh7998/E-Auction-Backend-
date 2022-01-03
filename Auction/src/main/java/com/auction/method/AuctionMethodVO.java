package com.auction.method;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuctionMethodVO {
	
	private Integer id;
	@Schema(defaultValue = "Auction Method Name", description = "enter name of auction method")
	@NotBlank(message = "{auctionMethod.required}")
	private String method;
	@JsonProperty("active")
	private boolean isActive;
	
	public AuctionMethod auctionMethodVOToAuctionMethod() {
		 AuctionMethod auctionMethod = new AuctionMethod();
		 auctionMethod.setActive(isActive);
		 auctionMethod.setId(id);
		 auctionMethod.setMethod(method);
		 return auctionMethod;
	}

}
