package com.auction.preparation;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.organization.item.OrganizationItem;

import lombok.Data;

@Entity
@Table(name = "auctionItems")
@Data
public class AuctionItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6111514022270546908L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double reservedPrice;
	
	private Double earnestMoney;
	
	private Double modifierValue;
	
	private Double modifierValueChangeAfter;
	
	private String itemDocument;
	
	@ManyToOne(fetch =  FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "organization_item_id")
	private OrganizationItem organizationItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AuctionPreparation auctionPreparation;
	
	public AuctionItemVO auctionItemToAuctionItemVO() {
		AuctionItemVO auctionItemVO = new AuctionItemVO();
		auctionItemVO.setEarnestMoney(earnestMoney);
		auctionItemVO.setId(id);
		auctionItemVO.setItemDocument(itemDocument);
		auctionItemVO.setModifierValue(modifierValue);
		auctionItemVO.setModifierValueChangeAfter(modifierValueChangeAfter);
		auctionItemVO.setReservedPrice(reservedPrice);
	  return auctionItemVO;
	}

}
