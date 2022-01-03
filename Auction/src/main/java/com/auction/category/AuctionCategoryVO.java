package com.auction.category;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuctionCategoryVO {
	
	private Integer id;
	@Schema(defaultValue = "Auction Category Name", description = "enter name of auction category")
	@NotBlank(message = "{auctionCategory.required}")
	private String category;
	@JsonProperty("active")
	private boolean isActive;
	
	public AuctionCategory auctionCategoryVOToAuctionCategory() {
		AuctionCategory auctionCategory = new AuctionCategory();
		auctionCategory.setActive(isActive);
		auctionCategory.setCategory(category);
		auctionCategory.setId(id);
		return auctionCategory;
	}

}
