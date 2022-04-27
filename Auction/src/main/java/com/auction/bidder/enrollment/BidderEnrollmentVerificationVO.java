package com.auction.bidder.enrollment;

import java.time.LocalDateTime;
import java.util.List;

import com.auction.preparation.AuctionPreparationVO;
import com.auction.user.UserVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data

public class BidderEnrollmentVerificationVO {

	@JsonProperty("auctionId")
	private Long auctionId;
	
	@JsonProperty("bidderEnrollmentId")
	private Long bidderEnrollmentid;
	
	@JsonProperty("isVerified")
	private Boolean isVerified;
}
