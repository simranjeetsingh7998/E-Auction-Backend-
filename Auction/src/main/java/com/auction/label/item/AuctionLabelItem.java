package com.auction.label.item;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.organization.item.OrganizationItem;
import com.auction.preparation.AuctionPreparation;

import lombok.Data;

@Entity
@Table(name = "auctionLabelItem")
@Data
public class AuctionLabelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6918127296617110074L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auctionPreparationId")
	private AuctionPreparation auctionPreparation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizationItemId")
	private OrganizationItem organizationItem;
	
	private String labelType;

}
