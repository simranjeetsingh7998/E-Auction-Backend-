package com.auction.preparation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import com.auction.properties.AuctionItemProprtiesVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionScheduleVO {
	
	@JsonProperty("start_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDateTime;
	
	@JsonProperty("end_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDateTime;
	
	@JsonProperty("properties")
	private List<AuctionItemProprtiesVO> auctionItemProprtiesVOs = new ArrayList<>();
	
	@Min(value = 1, message = "${auctionSchedule.auctionExtendTimeCondition}")
	@JsonProperty("auction_extend_minute_before_end_time")
	private Integer auctionExtendTimeCondition;
	
	@Min(value = 1, message = "${auctionSchedule.auctionExtendMinutes}")
	@JsonProperty("auction_extend_minutes")
	private Integer auctionExtendMinutes;
	
	@JsonProperty("extend_limit")
	private Integer auctionExtendLimit;

}
