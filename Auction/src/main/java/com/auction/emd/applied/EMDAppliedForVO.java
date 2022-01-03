package com.auction.emd.applied;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EMDAppliedForVO {
	
	private Integer id;
	@Schema(defaultValue = "EMD Applied For Mode", description = "select EMD Applied For")
	@NotBlank(message = "{emdAppliedFor.required}")
	@JsonProperty("emd_applied_for")
	private String emdAppliedFor;
	@JsonProperty("active")
	private boolean isActive;
	
	public EMDAppliedFor emdAppliedForVOToEMDAppliedFor() {
		 EMDAppliedFor appliedFor = new EMDAppliedFor();
		 appliedFor.setActive(isActive);
		 appliedFor.setEmdFor(emdAppliedFor);
		 appliedFor.setId(id);
		return appliedFor; 
	}

}
