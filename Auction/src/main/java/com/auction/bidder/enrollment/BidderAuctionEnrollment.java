package com.auction.bidder.enrollment;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.auction.preparation.AuctionPreparation;
import com.auction.user.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "bidderAuctionEnrollment", indexes = {
		@Index(columnList = "auction_preparation_id"),
		@Index(columnList = "participant")
})
@Data
@EqualsAndHashCode(of = "id")
public class BidderAuctionEnrollment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5958135268599159798L;
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AuctionPreparation auctionPreparation;
	
	private Double emdAmount;
	
	private Double eventProcessingFeeAmount;
	
	private Integer emdLimit;
	
	private String refundAccountBeneficiaryName;
	
	private String refundAccountNumber;
	
	private String refundAccountIfsccode;
	
	private LocalDateTime amountPaidAt;
	
	private String addressProof;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "participant")
	private User user;
	
	@OneToMany(mappedBy = "bidderAuctionEnrollment", cascade = CascadeType.ALL)
	private Set<JointHolder> jointHolders = new HashSet<>();
	
	@ColumnDefault("false")
	private boolean verified;
	
	@PrePersist
	public void setAmountPaidAt() {
		 this.amountPaidAt = LocalDateTime.now();
	}
	
	public void addJointHolder(JointHolder jointHolder) {
		 this.jointHolders.add(jointHolder);
		 jointHolder.setBidderAuctionEnrollment(this);
	}
	
	public BidderAuctionEnrollmentVO bidderAuctionEnrollmentToBidderAuctionEnrollmentVO() {
		  BidderAuctionEnrollmentVO auctionEnrollmentVO = new BidderAuctionEnrollmentVO();
		  auctionEnrollmentVO.setAmountPaidAt(amountPaidAt);
		  auctionEnrollmentVO.setEmdAmount(emdAmount);
		  auctionEnrollmentVO.setEmdLimit(emdLimit);
		  auctionEnrollmentVO.setEventProcessingFeeAmount(eventProcessingFeeAmount);
		  auctionEnrollmentVO.setId(id);
		  auctionEnrollmentVO.setRefundAccountBeneficiaryName(refundAccountBeneficiaryName);
		  auctionEnrollmentVO.setRefundAccountIfsccode(refundAccountIfsccode);
		  auctionEnrollmentVO.setRefundAccountNumber(refundAccountNumber);
		  auctionEnrollmentVO.setVerified(verified);
		return auctionEnrollmentVO;  
	}

}
