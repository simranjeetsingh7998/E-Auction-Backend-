package com.auction.process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class AuctionProcessService implements IAuctionProcessService {
	
	@Autowired
	private IAuctionProcessDao auctionProcessDao;

	@Override
	public List<AuctionProcessVO> findAllByIsActiveTrue() {
		return this.auctionProcessDao.findAllByIsActiveTrue().stream().map(AuctionProcess::auctionProcessToAuctionProcessVO).toList();
	}

	@Override
	public void addOrUpdate(AuctionProcessVO auctionProcessVO) {
		this.auctionProcessDao.save(auctionProcessVO.auctionProcessVOToAuctionProcess());
	}

	@Override
	public AuctionProcessVO findById(Integer id) {
		return this.findAuctionProcessById(id).auctionProcessToAuctionProcessVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		AuctionProcess auctionProcess = this.findAuctionProcessById(id);
		auctionProcess.setActive(false);
		this.auctionProcessDao.save(auctionProcess);
	}
	
	
	private AuctionProcess findAuctionProcessById(Integer id) {
		return this.auctionProcessDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Auction Process not found"));
	}
	

}
