package com.auction.item.template;

import java.util.Objects;

import com.auction.organization.OrganizationVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionItemTemplateVO {
	
	private Integer id;
	
	@JsonProperty("template_name")
	private String tName;
	
	private OrganizationVO organization;
	
	
	@JsonProperty("active")
	private boolean isActive;
	
	public AuctionItemTemplate auctionItemTemplateVOToAuctionItemTemplate() {
		 AuctionItemTemplate auctionItemTemplate = new AuctionItemTemplate();
		 if(!Objects.isNull(id) && id !=0)
			 auctionItemTemplate.setId(id);
		 
		 auctionItemTemplate.setActive(isActive);
		 auctionItemTemplate.setTName(tName);
		return auctionItemTemplate; 
	}
}
