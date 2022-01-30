package com.auction.property.type;

import com.auction.category.AuctionCategoryVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PropertyTypeVO {
	
	private Integer id;

	@JsonProperty("property_type")
	private String property;

	@JsonProperty("auction_category")
	private AuctionCategoryVO auctionCategory;
	
	@JsonProperty("status")
	private boolean isActive;
	
	public PropertyType propertyTypeVOToPropertyType() {
		 PropertyType propertyType = new PropertyType();
		 propertyType.setActive(isActive);
         propertyType.setId(id);
		 propertyType.setProperty(property);
		return propertyType; 
	}
}
