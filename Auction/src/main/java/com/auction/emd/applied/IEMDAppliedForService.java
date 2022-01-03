package com.auction.emd.applied;

import java.util.List;

public interface IEMDAppliedForService {
	
	List<EMDAppliedForVO> findAllByIsActiveTrue();
	
	void addOrUpdate(EMDAppliedForVO emdAppliedForVO);
	
	EMDAppliedForVO findById(Integer id);
	
	void deActivate(Integer id);

}
