package com.auction.method;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;

@Service
public class AuctionMethodService implements IAuctionMethodService{
	
	@Autowired
	private IAuctionMethodDao auctionMethodDao;

	@Override
	public List<AuctionMethodVO> findAllByIsActiveTrue() {
		return this.auctionMethodDao.findAllByIsActiveTrue().stream().map(AuctionMethod::auctionMethodToAuctionMethodVO).toList();
	}

	@Override
	public void addOrUpdate(AuctionMethodVO auctionMethodVO) {
		this.auctionMethodDao.save(auctionMethodVO.auctionMethodVOToAuctionMethod());
	}

	@Override
	public AuctionMethodVO findById(Integer id) {
		return this.findAuctionMethodById(id).auctionMethodToAuctionMethodVO();
	}

	@Transactional
	@Override
	public void deActivate(Integer id) {
		AuctionMethod auctionMethod = this.findAuctionMethodById(id);
		auctionMethod.setActive(false);
		this.auctionMethodDao.save(auctionMethod);
		
	}
	
	@Override
	public AuctionMethodVO getAuctionMethodForAuction(Long auctionPreparationId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionPreparationId);
		List<AuctionMethod> auctionMethodList = this.auctionMethodDao.findAllByAuctionPreparations(auctionPreparation);
		if(!Objects.isNull(auctionMethodList) && !auctionMethodList.isEmpty()) {
			 return auctionMethodList.get(0).auctionMethodToAuctionMethodVO();
		}
		return null;
	}
	
	private AuctionMethod findAuctionMethodById(Integer id) {
		return this.auctionMethodDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Auction method not found"));
	}

}
