package com.auction.sector;

import java.util.List;

public interface ISectorService {
	
	List<SectorVO> findAllByIsActiveTrue();
	
	void addOrUpdate(SectorVO sectorVO);
	
	SectorVO findById(Integer id);
	
	void deActivate(Integer id);

}
