package com.auction.division;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DivisionVO {
	
	private Integer id;
	@Schema(defaultValue = "Sonipat", description = "enter division")
	@NotBlank(message = "{divisionName.required}")
	@JsonProperty("division_name")
	private String divisionName;
	@JsonProperty("active")
	private boolean isActive;
	
    public Division divisionVOToDivision() {
    	 Division division = new Division();
    	 division.setActive(isActive);
    	 division.setDivisionName(divisionName);
    	 division.setId(id);
    	return division; 
    }

}
