package com.auction.covered.area;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CoveredAreaVO {
	
	private Integer id;
	@Schema(defaultValue = "Sonipat", description = "enter covered area")
	@NotBlank(message = "{coveredArea.required}")
	private String area;
	@JsonProperty("active")
	private boolean isActive;
	
    public CoveredArea coveredAreaVOToCoveredArea() {
    	 CoveredArea coveredArea = new CoveredArea();
    	 coveredArea.setActive(isActive);
    	 coveredArea.setArea(area);
    	 coveredArea.setId(id);
    	return coveredArea; 
    }

}
