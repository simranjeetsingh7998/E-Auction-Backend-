package com.auction.property.type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.category.AuctionCategory;

@Repository
public interface IPropertyTypeDao extends JpaRepository<PropertyType, Integer> {
	
	List<PropertyType> findAllByAuctionCategoryAndIsActiveTrue(AuctionCategory auctionCategory);

}
