package com.auction.process;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuctionProcessVO {
	
	private Integer id;
	@Schema(defaultValue = "Auction Process Name", description = "enter name of auction process")
	@NotBlank(message = "{auctionProcess.required}")
	private String process;
	@JsonProperty("active")
	private boolean isActive;
	
	public AuctionProcess auctionProcessVOToAuctionProcess() {
		AuctionProcess auctionProcess = new AuctionProcess();
		auctionProcess.setActive(isActive);
		auctionProcess.setId(id);
		auctionProcess.setProcess(process);
		return auctionProcess;
	}

}
