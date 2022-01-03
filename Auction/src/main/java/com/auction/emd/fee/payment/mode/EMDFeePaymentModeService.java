package com.auction.emd.fee.payment.mode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class EMDFeePaymentModeService implements IEMDFeePaymentModeService {
	
	@Autowired
	private IEMDFeePaymentModeDao emFeePaymentModeDao;

	@Override
	public List<EMDFeePaymentModeVO> findAllByIsActiveTrue() {
		return this.emFeePaymentModeDao.findAllByIsActiveTrue().stream().map(EMDFeePaymentMode::emdFeePaymentModeToEMDFeePaymentModeVO).toList();
	}

	@Override
	public void addOrUpdate(EMDFeePaymentModeVO emdFeePaymentModeVO) {
		this.emFeePaymentModeDao.save(emdFeePaymentModeVO.emdFeePaymentModeVOToEMDFeePaymentMode());
	}

	@Override
	public EMDFeePaymentModeVO findById(Integer id) {
		return this.findEMDFeePaymentModeById(id).emdFeePaymentModeToEMDFeePaymentModeVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		EMDFeePaymentMode emdFeePaymentMode = this.findEMDFeePaymentModeById(id);
		emdFeePaymentMode.setActive(false);
		this.emFeePaymentModeDao.save(emdFeePaymentMode);
	}
	
	
	private EMDFeePaymentMode findEMDFeePaymentModeById(Integer id) {
		return this.emFeePaymentModeDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("EMD Fee Payment Mode not found"));
	}
	

}
