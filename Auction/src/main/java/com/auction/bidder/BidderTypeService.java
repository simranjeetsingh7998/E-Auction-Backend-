package com.auction.bidder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class BidderTypeService implements IBidderTypeService {
	
	@Autowired
	private IBidderTypeDao bidderTypeDao;

	@Override
	public List<BidderTypeVO> findAllActiveBidderTypes() {
		return this.bidderTypeDao.findAllByIsActiveTrue().stream().map(BidderType::bidderTypeToBidderTypeVO).toList();
	}

	@Override
	public void saveBidderType(BidderTypeVO bidderTypeVO) {
		this.bidderTypeDao.save(bidderTypeVO.bidderTypeVOToBidderType());
	}

	@Override
	public BidderTypeVO findBidderTypeById(Integer id) {
		return this.findById(id).bidderTypeToBidderTypeVO();
	}
	
	private BidderType findById(Integer id) {
		 return this.bidderTypeDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bidder Type not exist"));
	}

}
