package com.auction.property.type;

import java.util.List;

public interface IPropertyTypeService {
	
	void save(PropertyTypeVO propertyTypeVO);
	
	List<PropertyTypeVO> findAllByAuctionCategory(Integer auctionCategoryId);
	
	void deactivate(Integer id);

}
