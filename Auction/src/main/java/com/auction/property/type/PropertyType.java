package com.auction.property.type;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.auction.category.AuctionCategory;
import com.auction.preparation.AuctionPreparation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "propertyType")
@Data
@EqualsAndHashCode(of = {"id"})
public class PropertyType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7326063776919959798L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 100)
	private String property;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	private AuctionCategory auctionCategory;
	
	@OneToMany(mappedBy = "propertyType", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AuctionPreparation> auctionPreparations = new HashSet<>();
	
	private boolean isActive;
	
	public PropertyTypeVO propertyTypeToPropertyTypeVO() {
		 PropertyTypeVO propertyTypeVO = new PropertyTypeVO();
		 propertyTypeVO.setActive(isActive);
		 propertyTypeVO.setId(id);
		 propertyTypeVO.setProperty(property);
		return propertyTypeVO; 
	}

}
