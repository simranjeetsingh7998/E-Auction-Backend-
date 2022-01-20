package com.auction.item.label.template.mapping;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.item.template.AuctionItemTemplate;
import com.auction.organization.item.label.master.ItemLabelMaster;

import lombok.Data;

@Entity
@Table(name = "auctionItemLabelTemplateMapping")
@Data
public class AuctionItemLabelTemplateMapping implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3096093628808944647L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer order;
	
    @ManyToOne(fetch = FetchType.LAZY)
    private AuctionItemTemplate auctionItemTemplate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemLabelMaster itemLabelMaster;

}
