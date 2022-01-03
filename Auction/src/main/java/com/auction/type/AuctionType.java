package com.auction.type;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "auctionType", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"aType"})
})
@Data
public class AuctionType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 493716115321038710L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String aType;
	private boolean isActive;
	
	public AuctionTypeVO auctionTypeToAuctionTypeVO() {
		 AuctionTypeVO auctionTypeVO = new AuctionTypeVO();
		 auctionTypeVO.setId(id);
		 auctionTypeVO.setAType(aType);
		 auctionTypeVO.setActive(isActive);
	   return auctionTypeVO;
	}

}
