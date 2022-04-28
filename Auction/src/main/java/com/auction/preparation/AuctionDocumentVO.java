package com.auction.preparation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionDocumentVO {
	
	private Long id;
	
	@JsonProperty("file_path")
	private String documentPath;
	
	private boolean exists;


}
