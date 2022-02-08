package com.auction.bidder.enrollment;

import java.time.LocalDateTime;

import com.auction.preparation.AuctionPreparationVO;
import com.auction.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BidderAuctionEnrollmentVO {
	
    private Long id;
	
    @JsonProperty("auction")
	private AuctionPreparationVO auctionPreparation;
	
    @JsonProperty("emd_amount")
	private Double emdAmount;
	
    @JsonProperty("security")
	private Double eventProcessingFeeAmount;
	
    @JsonProperty("emd_limit")
	private Integer emdLimit;
	
    @JsonProperty("amount_paid_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime amountPaidAt;
	
	private UserVO participant;
	
	public BidderAuctionEnrollment bidderAuctionEnrollmentVOToBidderAuctionEnrollment() {
		  BidderAuctionEnrollment auctionEnrollment = new BidderAuctionEnrollment();
		  auctionEnrollment.setAmountPaidAt(amountPaidAt);
		  auctionEnrollment.setEmdAmount(emdAmount);
		  auctionEnrollment.setEmdLimit(emdLimit);
		  auctionEnrollment.setEventProcessingFeeAmount(eventProcessingFeeAmount);
		  auctionEnrollment.setId(id);
		return auctionEnrollment;  
	}

}
