package com.auction.preparation;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.auction.properties.AuctionItemProprties;

import lombok.Data;

@Entity
@Table(name = "auctionProperties")
@Data
public class Properties {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AuctionPreparation auctionPreparation;
	
	@OneToOne(fetch = FetchType.LAZY)
	private AuctionItemProprties auctionItemProprties;

}
