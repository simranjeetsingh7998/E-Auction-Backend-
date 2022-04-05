package com.auction.properties;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.user.User;

import lombok.Data;

@Entity
@Table(name = "auctionItemUserProperites")
@Data
public class AuctionItemUserProperties {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch =  FetchType.LAZY)
	private AuctionItemProprties auctionItemProperties;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User bidder;
	
	private PropertiesStatus propertiesStatus;

}
