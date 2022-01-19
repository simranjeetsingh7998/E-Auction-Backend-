package com.auction.label.item;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionItemLabelVO {
	
	private Long id;
	
	@JsonProperty("auction_preparation_id")
	private Long auctionPreparationId;
	
	@JsonProperty("organization_item_id")
	private Long organizationItemId;
	
	@JsonProperty("label_type")
	private String labelType;

}
