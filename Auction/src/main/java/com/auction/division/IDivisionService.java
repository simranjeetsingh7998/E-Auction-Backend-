package com.auction.division;

import java.util.List;

public interface IDivisionService {
	
	List<DivisionVO> findAllByIsActiveTrue();
	
	void addOrUpdate(DivisionVO divisionVO);
	
	DivisionVO findById(Integer id);
	
	void deActivate(Integer id);

}
