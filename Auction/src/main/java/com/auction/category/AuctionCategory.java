package com.auction.category;

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

import com.auction.property.type.PropertyType;

import lombok.Data;

@Entity
@Table(name = "auctionCategory", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"category"})
})
@Data
public class AuctionCategory implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3797682357017119789L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 30)
	private String category;
	private boolean isActive;
	
	@OneToMany(mappedBy = "auctionCategory", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PropertyType> propertyTypes = new HashSet<>();
	
	public AuctionCategoryVO auctionCategoryToAuctionCategoryVO() {
		AuctionCategoryVO auctionCategoryVO = new AuctionCategoryVO();
		auctionCategoryVO.setActive(isActive);
		auctionCategoryVO.setCategory(category);
		auctionCategoryVO.setId(id);
		return auctionCategoryVO;
	}

}
