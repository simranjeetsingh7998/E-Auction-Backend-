package com.auction.preparation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.auction.JpaAuditConfig;
import com.auction.bid.submission.placement.BidSubmissionPlacement;
import com.auction.emd.applied.EMDAppliedFor;
import com.auction.emd.fee.payment.mode.EMDFeePaymentMode;
import com.auction.event.processing.fee.mode.EventProcessingFeeMode;
import com.auction.item.template.AuctionItemTemplate;
import com.auction.jpa.audit.AuditMetadata;
import com.auction.method.AuctionMethod;
import com.auction.process.AuctionProcess;
import com.auction.property.type.PropertyType;
import com.auction.type.AuctionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "auctionPreparation")
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@EntityListeners(value = JpaAuditConfig.class)
public class AuctionPreparation extends AuditMetadata implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6488145854776740929L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String auctionName;
	
	private String referenceNumber;
	
	@Column(columnDefinition = "text")
	private String description;
	
	private String unitDivision;
	
	private Double eventProcessingFeeAmount;
	
	private Double emdFeeAmount;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime registrationStartDateTime;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime registrationEndDateTime;
	
	private String auctionDocument;
	
	private String noticeDocument;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "auction_type_id")
	private AuctionType auctionType;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private AuctionMethod auctionMethod;
	
//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//	private AuctionCategory auctionCategory;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private PropertyType propertyType;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private AuctionProcess auctionProcess;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private BidSubmissionPlacement bidSubmissionPlacement;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private EventProcessingFeeMode eventProcessingFeeMode;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private EMDFeePaymentMode emdFeePaymentMode;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private EMDAppliedFor emdAppliedFor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AuctionItemTemplate auctionItemTemplate;
	
	@OneToMany(mappedBy = "auctionPreparation", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuctionItem> auctionItems = new HashSet<>();
	
	private AuctionStatus auctionStatus;
	
	@ColumnDefault("true")
	private boolean isActive;
	
	public void addAuctionItem(AuctionItem auctionItem) {
		this.auctionItems.add(auctionItem);
		auctionItem.setAuctionPreparation(this);
	}
	
	public AuctionPreparationVO auctionPreparationToAuctionPreparationVO() {
		   AuctionPreparationVO auctionPreparationVO = new AuctionPreparationVO();
		   auctionPreparationVO.setActive(isActive);
		   auctionPreparationVO.setAuctionDocument(auctionDocument);
		   auctionPreparationVO.setAuctionStatus(auctionStatus);
		   auctionPreparationVO.setDescription(description);
		   auctionPreparationVO.setEmdFeeAmount(emdFeeAmount);
		   auctionPreparationVO.setEventProcessingFeeAmount(eventProcessingFeeAmount);
		   auctionPreparationVO.setId(id);
		   auctionPreparationVO.setNoticeDocument(noticeDocument);
		   auctionPreparationVO.setReferenceNumber(referenceNumber);
		   auctionPreparationVO.setRegistrationEndDateTime(registrationEndDateTime);
		   auctionPreparationVO.setRegistrationStartDateTime(registrationStartDateTime);
		   auctionPreparationVO.setUnitDivision(unitDivision);
		   auctionPreparationVO.setAuctionName(auctionName);
		   return auctionPreparationVO;
	}
}
