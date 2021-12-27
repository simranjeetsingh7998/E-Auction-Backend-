package com.auction.bidder.category;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.auction.bidder.BidderType;
import com.auction.user.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "bidderCategory", 
indexes = {@Index(columnList = "bCategory")})
@Data
@EqualsAndHashCode(of = {"id"})
public class BidderCategory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1014081483560752545L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 20)
	private String bCategory;
	
	private boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private BidderType bidderType;
	
	@OneToMany(mappedBy = "bidderCategory", cascade = CascadeType.MERGE, orphanRemoval = true)
	private Set<User> users = new HashSet<>();
	
	public BidderCategoryVO bidderCategoryToBidderCategoryVO() {
		BidderCategoryVO bidderCategoryVO = new BidderCategoryVO();
		bidderCategoryVO.setId(id);
		bidderCategoryVO.setActive(isActive);
		bidderCategoryVO.setBCategory(bCategory);
	   return bidderCategoryVO;	
	}
	

}
