package com.auction.bidding;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.auction.preparation.AuctionPreparation;
import com.auction.user.User;

import lombok.Data;

@Entity
@Table(name = "bidding")
@Data
public class Bidding implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2849706165274368631L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private AuctionPreparation auctionPreparation;
	
	private double biddingAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User bidder;
	
	@Column(columnDefinition = "TIMESTAMP(3)")
	private LocalDateTime biddingAt;
	
	@Column(length = 20)
	private String roundNo;
	
	private LocalDateTime roundStartAt;
	
	private LocalDateTime roundClosedAt;
	
	public BiddingVO biddingToBiddingVO() {
		 BiddingVO biddingVO = new BiddingVO();
		 biddingVO.setBiddingAmount(biddingAmount);
		 biddingVO.setBiddingAt(biddingAt);
		 biddingVO.setId(id);
		 biddingVO.setRound(roundNo);
	  return biddingVO;
	}

}
