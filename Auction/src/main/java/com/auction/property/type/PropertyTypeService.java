package com.auction.property.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.category.AuctionCategory;
import com.auction.global.exception.ResourceNotFoundException;

@Service
@Transactional
public class PropertyTypeService implements IPropertyTypeService {
	
	@Autowired
	private IPropertyTypeDao propertyTypeDao;
	
	@Override
	public void save(PropertyTypeVO propertyTypeVO) {
		PropertyType propertyType = propertyTypeVO.propertyTypeVOToPropertyType();
		propertyType.setAuctionCategory(propertyTypeVO.getAuctionCategory().auctionCategoryVOToAuctionCategory());
		this.propertyTypeDao.save(propertyType);
	}
	
	@Override
	public List<PropertyTypeVO> findAllByAuctionCategory(Integer auctionCategoryId) {
		AuctionCategory auctionCategory = new AuctionCategory();
		auctionCategory.setId(auctionCategoryId);
		return this.propertyTypeDao.findAllByAuctionCategoryAndIsActiveTrue(auctionCategory)
				.stream().map(PropertyType::propertyTypeToPropertyTypeVO).toList();
	}
	
	@Override
	public void deactivate(Integer id) {
		PropertyType propertyType = this.propertyTypeDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property Type not found"));
	    propertyType.setActive(false);
	    this.propertyTypeDao.save(propertyType);
	}

}
