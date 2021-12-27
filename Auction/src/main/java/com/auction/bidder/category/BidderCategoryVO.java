package com.auction.bidder.category;

import com.auction.bidder.BidderTypeVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class BidderCategoryVO {
	
	private Integer id;
	
	@Schema(defaultValue = "General", description = "enter categoey of bidder")
	@JsonProperty("bidder_category")
	private String bCategory;
	
	@Schema(defaultValue = "true", description = "enter status of bidder category")
	@JsonProperty("active")
	private boolean isActive;
	
	@Schema(defaultValue = "{id : 1}", description = "enter bidder type of bidder category")
	@JsonProperty("bidder_type")
	private BidderTypeVO bidderType;
	
	public BidderCategory bidderCategoryVOToBidderCategory() {
		BidderCategory bidderCategory = new BidderCategory();
		bidderCategory.setId(id);
		bidderCategory.setActive(isActive);
		bidderCategory.setBCategory(bCategory);
	   return bidderCategory;	
	}

}
