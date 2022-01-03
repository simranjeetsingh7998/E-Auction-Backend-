package com.auction.emd.fee.payment.mode;

import java.util.List;

public interface IEMDFeePaymentModeService {
	
	List<EMDFeePaymentModeVO> findAllByIsActiveTrue();
	
	void addOrUpdate(EMDFeePaymentModeVO emdFeePaymentModeVO);
	
	EMDFeePaymentModeVO findById(Integer id);
	
	void deActivate(Integer id);

}
