package com.auction.bid.submission.placement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.auction.preparation.AuctionPreparation;

import lombok.Data;

@Entity
@Table(name = "bidSubmissionPlacement", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"bidSubmissionPlacementType"})
})
@Data
public class BidSubmissionPlacement implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1887363614251688092L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String bidSubmissionPlacementType;
	private boolean isActive;
	
	@OneToMany(mappedBy = "bidSubmissionPlacement", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuctionPreparation> auctionPreparations = new HashSet<>();
	
	public BidSubmissionPlacementVO bidSubmissionPlacementToBidSubmissionPlacementVO() {
		 BidSubmissionPlacementVO bidSubmissionPlacementVO = new BidSubmissionPlacementVO();
		 bidSubmissionPlacementVO.setActive(isActive);
		 bidSubmissionPlacementVO.setBidSubmissionPlacementType(bidSubmissionPlacementType);
		 bidSubmissionPlacementVO.setId(id);
		 return bidSubmissionPlacementVO;
	}

}
