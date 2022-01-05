package com.auction.sector;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SectorVO {
	
	private Integer id;
	@Schema(defaultValue = "Sector-14", description = "enter Sector")
	@NotBlank(message = "{sectorName.required}")
	@JsonProperty("sector_name")
	private String sectorName;
	@JsonProperty("active")
	private boolean isActive;
	
    public Sector sectorVOToSector() {
    	 Sector sector = new Sector();
    	 sector.setActive(isActive);
    	 sector.setSectorName(sectorName);
    	 sector.setId(id);
    	return sector; 
    }

}
