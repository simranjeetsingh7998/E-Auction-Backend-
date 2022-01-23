package com.auction.item.label.template.mapping;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.item.template.AuctionItemTemplate;
import com.auction.organization.item.label.master.ItemLabelMaster;

@Repository
public interface IAuctionItemLabelTemplateMappingDao extends JpaRepository<AuctionItemLabelTemplateMapping, Integer> {
	
	boolean existsByLabelOrderAndAuctionItemTemplateAndItemLabelMaster(Integer order, AuctionItemTemplate auctionItemTemplate,
			ItemLabelMaster itemLabelMaster);
	
	boolean existsByLabelOrderAndAuctionItemTemplate(Integer order, AuctionItemTemplate auctionItemTemplate);
	
	boolean existsByAuctionItemTemplateAndItemLabelMaster(AuctionItemTemplate auctionItemTemplate,
			ItemLabelMaster itemLabelMaster);
	
	List<AuctionItemLabelTemplateMapping> findAllByAuctionItemTemplate(AuctionItemTemplate auctionItemTemplate, Sort orderBy);

}
