package com.auction.emd.applied;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class EMDAppliedForService implements IEMDAppliedForService {
	
	@Autowired
	private IEMDAppliedForDao emdAppliedForDao;

	@Override
	public List<EMDAppliedForVO> findAllByIsActiveTrue() {
		return this.emdAppliedForDao.findAllByIsActiveTrue().stream().map(EMDAppliedFor::emdAppliedForToEMDAppliedForVO).toList();
	}

	@Override
	public void addOrUpdate(EMDAppliedForVO emdAppliedForVO) {
		this.emdAppliedForDao.save(emdAppliedForVO.emdAppliedForVOToEMDAppliedFor());
	}

	@Override
	public EMDAppliedForVO findById(Integer id) {
		return this.findEMDAppliedForVOById(id).emdAppliedForToEMDAppliedForVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		EMDAppliedFor emdAppliedFor = this.findEMDAppliedForVOById(id);
		emdAppliedFor.setActive(false);
		this.emdAppliedForDao.save(emdAppliedFor);
	}
	
	
	private EMDAppliedFor findEMDAppliedForVOById(Integer id) {
		return this.emdAppliedForDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("EMD Applied For not found"));
	}
	

}
