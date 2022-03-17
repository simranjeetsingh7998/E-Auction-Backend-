package com.auction.preparation;

import java.time.LocalDateTime;

public interface AuctionPreparationCronDTO {
	
	public Long getId();
	public LocalDateTime getAuctionFinishTime();
	public Integer getIntervalInMinutes();

}
