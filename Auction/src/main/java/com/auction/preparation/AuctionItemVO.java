package com.auction.preparation;

import com.auction.organization.item.OrganizationItemVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionItemVO {
	
    private Long id;
	
    @JsonProperty("reserved_price")
	private Double reservedPrice;
	
    @JsonProperty("earnest_money")
	private Double earnestMoney;
	
    @JsonProperty("modifier_value")
	private Double modifierValue;
	
    @JsonProperty("modifier_value_change_after")
	private Double modifierValueChangeAfter;
	
    @JsonProperty("item_document")
	private String itemDocument;
	
    @JsonProperty("organization_item")
	private OrganizationItemVO organizationItem;
    
	public AuctionItem auctionItemVOToAuctionItem() {
		AuctionItem auctionItem = new AuctionItem();
		auctionItem.setEarnestMoney(earnestMoney);
		auctionItem.setId(id);
		auctionItem.setItemDocument(itemDocument);
		auctionItem.setModifierValue(modifierValue);
		auctionItem.setModifierValueChangeAfter(modifierValueChangeAfter);
		auctionItem.setReservedPrice(reservedPrice);
	  return auctionItem;
	}


}
