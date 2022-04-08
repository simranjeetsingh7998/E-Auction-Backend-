package com.auction.preparation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.auction.bid.submission.placement.BidSubmissionPlacementVO;
import com.auction.emd.applied.EMDAppliedForVO;
import com.auction.emd.fee.payment.mode.EMDFeePaymentModeVO;
import com.auction.event.processing.fee.mode.EventProcessingFeeModeVO;
import com.auction.item.template.AuctionItemTemplateVO;
import com.auction.method.AuctionMethodVO;
import com.auction.process.AuctionProcessVO;
import com.auction.property.type.PropertyTypeVO;
import com.auction.type.AuctionTypeVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AuctionPreparationVO {
	
	private Long id;
	
	@JsonProperty("auction_name")
	private String auctionName;
	
	@JsonProperty("auction_type")
	private AuctionTypeVO auctionType;

	@JsonProperty("auction_method")
	private AuctionMethodVO auctionMethod;
	
	@JsonProperty("reference_number")
	private String referenceNumber;
	
	private String description;
	
    @JsonProperty("emd_limit")
    private String emdLimit;
	
//	@JsonProperty("auction_category")
//	private AuctionCategoryVO auctionCategory;
	
	@JsonProperty("property_type")
	private PropertyTypeVO propertyTypeVO;
	
	@JsonProperty("auction_process")
	private AuctionProcessVO auctionProcess;
	
	@JsonProperty("unit_division")
	private String unitDivision;
	
	@JsonProperty("bidder_submission_placement")
	private BidSubmissionPlacementVO bidSubmissionPlacement;
	
	@JsonProperty("event_processing_fee_mode")
	private EventProcessingFeeModeVO eventProcessingFeeMode;
	
	@JsonProperty("event_processing_fee_amount")
	private Double eventProcessingFeeAmount;
	
	@JsonProperty("emd_fee_payment_mode")
	private EMDFeePaymentModeVO emdFeePaymentMode;

	@JsonProperty("emd_applied_for")
	private EMDAppliedForVO emdAppliedFor;
	
	@JsonProperty("emd_fee_amount")
	private Double emdFeeAmount;
	
	@JsonProperty("auction_items")
	private List<AuctionItemVO> auctionItems = new ArrayList<>();
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("registration_start_date_time")
	private LocalDateTime registrationStartDateTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("registration_end_date_time")
	private LocalDateTime registrationEndDateTime;
	
	@JsonProperty("auction_document")
	private String auctionDocument;
	
	@JsonProperty("notice_document")
	private String noticeDocument;
	
	@JsonProperty("status")
	private AuctionStatus auctionStatus;
	
	@JsonProperty("template")
	private AuctionItemTemplateVO auctionItemTemplateVO;
	
	@JsonProperty("active")
	private boolean isActive;
	
	@JsonProperty("bidder_count")
	private int bidderCount;
	
	@JsonProperty("emd_count")
	private int emdCount;
	
	@JsonProperty("interval_minutes")
	private int intervalInMinutes;
	
	@JsonProperty("auction_schedule")
	private AuctionScheduleVO auctionScheduleVO;
	
	public AuctionPreparation auctionPreparationVOToAuctionPreparation() {
		   AuctionPreparation auctionPreparation = new AuctionPreparation();
		   auctionPreparation.setActive(isActive);
		   auctionPreparation.setAuctionDocument(auctionDocument);
		   auctionPreparation.setAuctionStatus(auctionStatus);
		   auctionPreparation.setDescription(description);
		   auctionPreparation.setEmdFeeAmount(emdFeeAmount);
		   auctionPreparation.setEventProcessingFeeAmount(eventProcessingFeeAmount);
		   auctionPreparation.setId(id);
		   auctionPreparation.setNoticeDocument(noticeDocument);
		   auctionPreparation.setReferenceNumber(referenceNumber);
		   auctionPreparation.setRegistrationEndDateTime(registrationEndDateTime);
		   auctionPreparation.setRegistrationStartDateTime(registrationStartDateTime);
		   auctionPreparation.setUnitDivision(unitDivision);
		   auctionPreparation.setAuctionName(auctionName);
		   auctionPreparation.setEmdLimit(emdLimit);
		   auctionPreparation.setIntervalInMinutes(intervalInMinutes);
		   return auctionPreparation;
	}

}
