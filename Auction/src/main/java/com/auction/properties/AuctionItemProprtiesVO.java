package com.auction.properties;

import java.time.LocalDateTime;

import com.auction.organization.item.OrganizationItemVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionItemProprtiesVO {
	
    private Long id;
	
	private String property;

	@JsonProperty("organization_item")
	private OrganizationItemVO organizationItem;
	
	@JsonProperty("active")
	private boolean isActive;
	
//	@JsonProperty("sold")
//	private boolean isSold;
	@JsonProperty("property_status")
	private PropertiesStatus propertiesStatus;
	
	@JsonProperty("sold_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime soldDateTime;
	
	public AuctionItemProprties auctionItemProprtiesVOToAuctionItemProprties() {
		 AuctionItemProprties auctionItemProprties = new AuctionItemProprties();
		 auctionItemProprties.setId(id);
		 auctionItemProprties.setActive(isActive);
		 auctionItemProprties.setProperty(property);
		return auctionItemProprties; 
	}

}
