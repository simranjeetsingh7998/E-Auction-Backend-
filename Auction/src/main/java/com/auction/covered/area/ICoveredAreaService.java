package com.auction.covered.area;

import java.util.List;

public interface ICoveredAreaService {
	
	List<CoveredAreaVO> findAllByIsActiveTrue();
	
	void addOrUpdate(CoveredAreaVO coveredAreaVO);
	
	CoveredAreaVO findById(Integer id);
	
	void deActivate(Integer id);

}
