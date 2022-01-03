package com.auction.bid.submission.placement;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BidSubmissionPlacementVO {
	
	private Integer id;
	@Schema(defaultValue = "Bid Submission/Placement Value", description = "enter Bid Submission/Placement")
	@NotBlank(message = "{bidSubmissionPlacementType.required}")
	@JsonProperty("bid_submission_placement_type")
	private String bidSubmissionPlacementType;
	@JsonProperty("active")
	private boolean isActive;
	

	public BidSubmissionPlacement bidSubmissionPlacementVOToBidSubmissionPlacement() {
		 BidSubmissionPlacement bidSubmissionPlacement = new BidSubmissionPlacement();
		 bidSubmissionPlacement.setActive(isActive);
		 bidSubmissionPlacement.setBidSubmissionPlacementType(bidSubmissionPlacementType);
		 bidSubmissionPlacement.setId(id);
		 return bidSubmissionPlacement;
	}

}
