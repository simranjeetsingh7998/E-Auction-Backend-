package com.auction.preparation;

import java.time.LocalDateTime;
import com.auction.user.UserVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReturnReasonVO {
	
    private Long id;
	
	private String reason;
	
	@JsonProperty("user")
	private UserVO returnBy;
	
	@JsonProperty("return_at")
	private LocalDateTime returnAt;
	
	public ReturnReason returnReasonVOToReturnReason() {
		ReturnReason returnReason = new ReturnReason();
		returnReason.setId(id);
		returnReason.setReason(this.reason);
	  return returnReason;	
	}

}
