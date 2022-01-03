package com.auction.bid.submission.placement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class BidSubmissionPlacementService implements IBidSubmissionPlacementService {
	
	@Autowired
	private IBidSubmissionPlacementDao bidSubmissionPlacementDao;

	@Override
	public List<BidSubmissionPlacementVO> findAllByIsActiveTrue() {
		return this.bidSubmissionPlacementDao.findAllByIsActiveTrue().stream().map(BidSubmissionPlacement::bidSubmissionPlacementToBidSubmissionPlacementVO).toList();
	}

	@Override
	public void addOrUpdate(BidSubmissionPlacementVO bidSubmissionPlacementVO) {
		this.bidSubmissionPlacementDao.save(bidSubmissionPlacementVO.bidSubmissionPlacementVOToBidSubmissionPlacement());
	}

	@Override
	public BidSubmissionPlacementVO findById(Integer id) {
		return findBidSubmissionPlacementById(id).bidSubmissionPlacementToBidSubmissionPlacementVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		BidSubmissionPlacement bidSubmissionPlacement = this.findBidSubmissionPlacementById(id);
		bidSubmissionPlacement.setActive(false);
		this.bidSubmissionPlacementDao.save(bidSubmissionPlacement);
	}
	
	
	private BidSubmissionPlacement findBidSubmissionPlacementById(Integer id) {
		return this.bidSubmissionPlacementDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Bid Submission/Placement not found"));
	}
	

}
