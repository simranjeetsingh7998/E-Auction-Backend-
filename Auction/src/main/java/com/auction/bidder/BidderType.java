package com.auction.bidder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.auction.bidder.category.BidderCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "bidderType", indexes = { @Index(columnList = "isActive")}, uniqueConstraints = {
		@UniqueConstraint(columnNames = "bType")
})
@Data
@EqualsAndHashCode(of = "id")
public class BidderType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6451309878977701763L;

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private int id;
	
	private String bType;
	
	private boolean isActive;
	
//	@OneToMany(mappedBy = "bidderType", cascade = CascadeType.MERGE, orphanRemoval = true)
//	private Set<User> users = new HashSet<>();
	
	@OneToMany(mappedBy = "bidderType", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BidderCategory> bidderCategories = new HashSet<>();
	
	public BidderTypeVO bidderTypeToBidderTypeVO() {
		 BidderTypeVO bidderTypeVO = new BidderTypeVO();
		 bidderTypeVO.setId(id);
		 bidderTypeVO.setBType(bType);
		 bidderTypeVO.setActive(isActive);
		return bidderTypeVO; 
	}
	

}
