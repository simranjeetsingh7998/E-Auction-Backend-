package com.auction.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class AuctionTypeService implements IAuctionTypeService {
	
	@Autowired
	private IAuctionTypeDao auctionTypeDao;

	@Override
	public List<AuctionTypeVO> findAllByIsActiveTrue() {
		return this.auctionTypeDao.findAllByIsActiveTrue().stream().map(AuctionType::auctionTypeToAuctionTypeVO).toList();
	}

	@Override
	public void addOrUpdate(AuctionTypeVO auctionTypeVO) {
		this.auctionTypeDao.save(auctionTypeVO.auctionTypeVOToAuctionType());
	}

	@Override
	public AuctionTypeVO findById(Integer id) {
		return findAuctionTypeById(id).auctionTypeToAuctionTypeVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		AuctionType auctionType = this.findAuctionTypeById(id);
		auctionType.setActive(false);
		this.auctionTypeDao.save(auctionType);
	}
	
	
	private AuctionType findAuctionTypeById(Integer id) {
		return this.auctionTypeDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Auction Type not found"));
	}
	

}
