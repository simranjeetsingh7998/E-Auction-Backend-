package com.auction.properties;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.organization.item.OrganizationItem;

import lombok.Data;

@Entity
@Table(name = "auctionItemProperites")
@Data
public class AuctionItemProprties  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6242275698309514322L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private String property;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private OrganizationItem organizationItem;
	
	private boolean isSold;
	
	private LocalDateTime soldDateTime;
	
	private boolean isActive;
	
	public AuctionItemProprtiesVO auctionItemProprtiesToAuctionItemProprtiesVO() {
		 AuctionItemProprtiesVO auctionItemProprtiesVO = new AuctionItemProprtiesVO();
		 auctionItemProprtiesVO.setId(id);
		 auctionItemProprtiesVO.setActive(isActive);
		 auctionItemProprtiesVO.setProperty(property);
		 auctionItemProprtiesVO.setSold(isSold);
		 auctionItemProprtiesVO.setSoldDateTime(soldDateTime);
		return auctionItemProprtiesVO; 
	}

}
