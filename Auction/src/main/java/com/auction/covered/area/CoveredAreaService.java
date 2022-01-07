package com.auction.covered.area;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class CoveredAreaService implements ICoveredAreaService {
	
	@Autowired
	private ICoveredAreaDao coveredAreaDao;

	@Override
	public List<CoveredAreaVO> findAllByIsActiveTrue() {
		return this.coveredAreaDao.findAllByIsActiveTrue().stream().map(CoveredArea::coveredAreaToCoveredAreaVO).toList();
	}

	@Override
	public void addOrUpdate(CoveredAreaVO coveredAreaVO) {
		this.coveredAreaDao.save(coveredAreaVO.coveredAreaVOToCoveredArea());
	}

	@Override
	public CoveredAreaVO findById(Integer id) {
		return findCoveredAreaById(id).coveredAreaToCoveredAreaVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		CoveredArea CoveredArea = this.findCoveredAreaById(id);
		CoveredArea.setActive(false);
		this.coveredAreaDao.save(CoveredArea);
	}
	
	
	private CoveredArea findCoveredAreaById(Integer id) {
		return this.coveredAreaDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Covered Area not found"));
	}
	

}
