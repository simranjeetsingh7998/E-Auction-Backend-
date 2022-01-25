package com.auction.preparation;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuctionPreparationSearchParam {
	
	private LocalDateTime registrationStartDateTime;
	
	private LocalDateTime registrationEndDateTime;
	
	private String description;

}
