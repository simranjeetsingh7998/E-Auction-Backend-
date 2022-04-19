package com.auction.preparation;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name ="auctionExtendedHistory")
@Data
public class AuctionExtendedHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int round;
	
	private int extendCount;
	
	private Long auctionPreparation;
	
//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//	private AuctionPreparation preparation;

}
