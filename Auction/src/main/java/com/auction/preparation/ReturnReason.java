package com.auction.preparation;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.auction.user.User;

import lombok.Data;

@Entity
@Table(name = "auctionReturn")
@Data
public class ReturnReason implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1841507570731398759L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "text", name = "return_reason")
	private String reason;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private User returnBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AuctionPreparation auctionPreparation;
	
	private LocalDateTime returnAt;
	
	@PrePersist
	private void prePersist() {
		 this.returnAt = LocalDateTime.now();
	}
	
	public ReturnReasonVO returnReasonToReturnReasonVO() {
		ReturnReasonVO returnReasonVO = new ReturnReasonVO();
		returnReasonVO.setId(id);
		returnReasonVO.setReason(this.reason);
	  return returnReasonVO;	
	}

}
