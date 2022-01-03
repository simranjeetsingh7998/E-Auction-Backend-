package com.auction.event.processing.fee.mode;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EventProcessingFeeModeVO {
	
	private Integer id;
	@Schema(defaultValue = "Event Processing Fee Mode", description = "select Event Processing Fee Mode")
	@NotBlank(message = "{epfMode.required}")
	@JsonProperty("epf_mode")
	private String epfMode;
	@JsonProperty("active")
	private boolean isActive;
	
    public EventProcessingFeeMode eventProcessingFeeModeVOToEventProcessingFeeMode() {
    	 EventProcessingFeeMode eventProcessingFeeMode = new EventProcessingFeeMode();
    	 eventProcessingFeeMode.setActive(isActive);
    	 eventProcessingFeeMode.setEpfMode(epfMode);
    	 eventProcessingFeeMode.setId(id);
    	return eventProcessingFeeMode; 
    }

}
