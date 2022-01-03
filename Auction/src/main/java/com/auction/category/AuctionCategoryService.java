package com.auction.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class AuctionCategoryService implements IAuctionCategoryService{
	
	@Autowired
	private IAuctionCategoryDao auctionCategoryDao;

	@Override
	public List<AuctionCategoryVO> findAllByIsActiveTrue() {
		return this.auctionCategoryDao.findAllByIsActiveTrue().stream().map(AuctionCategory::auctionCategoryToAuctionCategoryVO).toList();
	}

	@Override
	public void addOrUpdate(AuctionCategoryVO auctionCategoryVO) {
		this.auctionCategoryDao.save(auctionCategoryVO.auctionCategoryVOToAuctionCategory());
	}

	@Override
	public AuctionCategoryVO findById(Integer id) {
		return this.findAuctionCategoryById(id).auctionCategoryToAuctionCategoryVO();
	}

	@Transactional
	@Override
	public void deActivate(Integer id) {
		AuctionCategory auctionCategory = this.findAuctionCategoryById(id);
		auctionCategory.setActive(false);
		this.auctionCategoryDao.save(auctionCategory);
		
	}
	
	private AuctionCategory findAuctionCategoryById(Integer id) {
		return this.auctionCategoryDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Auction Category not found"));
	}

}
