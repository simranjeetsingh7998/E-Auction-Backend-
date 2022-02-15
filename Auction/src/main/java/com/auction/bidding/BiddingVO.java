package com.auction.bidding;

import java.time.LocalDateTime;

import com.auction.preparation.AuctionPreparationVO;
import com.auction.user.UserVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BiddingVO {
	
    private Long id;
	
    @JsonProperty("auction")
	private AuctionPreparationVO auctionPreparation;
	
    @JsonProperty("amount")
	private double biddingAmount;
    
    @JsonProperty("modifier_value_multiply_by")
    private int modifierValueMultiplyBy;
	
	private UserVO bidder;
	
//	@Null
	@JsonProperty("bidding_at")
   // @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.sss")
	private LocalDateTime biddingAt;

}
