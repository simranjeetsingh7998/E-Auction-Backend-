package com.auction.station;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StationVO {
	
	private Integer id;
	@Schema(defaultValue = "Sonipat", description = "enter station")
	@NotBlank(message = "{stationName.required}")
	@JsonProperty("station_name")
	private String stationName;
	@JsonProperty("active")
	private boolean isActive;
	
    public Station stationVOToStation() {
    	 Station station = new Station();
    	 station.setActive(isActive);
    	 station.setStationName(stationName);
    	 station.setId(id);
    	return station; 
    }

}
