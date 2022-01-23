package com.auction.item.label.template.mapping;

import java.util.List;

import com.auction.item.template.AuctionItemTemplate;
import com.auction.organization.item.label.master.ItemLabelMaster;
import com.auction.organization.item.label.master.ItemLabelMasterVO;

public interface IAuctionItemLabelTemplateMappingService {

	void save(AuctionItemLabelTemplateMappingVO auctionItemLabelTemplateMappingVO);
	
	boolean existByLabelOrderAndAuctionItemTemplateAndItemLabelMaster(Integer order,AuctionItemTemplate auctionItemTemplate,
			ItemLabelMaster itemLabelMaster);
	
	void deleteById(Integer id);
	
	List<ItemLabelMasterVO> findAllByAuctionItemTemplate(Integer auctionItemTemplateId);
	
}
