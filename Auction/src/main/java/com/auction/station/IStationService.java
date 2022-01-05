package com.auction.station;

import java.util.List;

public interface IStationService {
	
	List<StationVO> findAllByIsActiveTrue();
	
	void addOrUpdate(StationVO stationVO);
	
	StationVO findById(Integer id);
	
	void deActivate(Integer id);

}
