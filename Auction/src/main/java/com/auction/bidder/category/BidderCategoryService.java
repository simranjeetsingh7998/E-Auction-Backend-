package com.auction.bidder.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidderCategoryService implements IBidderCategoryService {
	
	@Autowired
	private IBidderCategoryDao bidderCategoryDao;

	@Transactional
	@Override
	public void save(BidderCategoryVO bidderCategoryVO) {
		BidderCategory bidderCategory = bidderCategoryVO.bidderCategoryVOToBidderCategory();
		bidderCategory.setBidderType(bidderCategoryVO.getBidderType().bidderTypeVOToBidderType());
		this.bidderCategoryDao.save(bidderCategory);
	}

	@Override
	public List<BidderCategoryVO> findByBidderTypeId(Integer id) {
	   return	convertEntityToVO(this.bidderCategoryDao.findAllByIsActiveTrueAndBidderTypeId(id));
	}
	
	@Override
	public List<BidderCategoryVO> findAll() {
		return convertEntityToVO(this.bidderCategoryDao.findAll());
	}
	
	private List<BidderCategoryVO> convertEntityToVO(List<BidderCategory> bidderCategories){
		return bidderCategories.stream()
		   .map(BidderCategory::bidderCategoryToBidderCategoryVO).toList();
	}

}
