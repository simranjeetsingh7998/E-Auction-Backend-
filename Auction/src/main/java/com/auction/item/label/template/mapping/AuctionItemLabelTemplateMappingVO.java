package com.auction.item.label.template.mapping;

import com.auction.item.template.AuctionItemTemplateVO;
import com.auction.organization.item.label.master.ItemLabelMasterVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionItemLabelTemplateMappingVO {
	
	private Integer id;
	
	@JsonProperty("order")
	private Integer labelOrder;
	
	@JsonProperty("template")
    private AuctionItemTemplateVO auctionItemTemplate;
    
	@JsonProperty("label_master")
    private ItemLabelMasterVO itemLabelMaster;

}
