package com.auction.station;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class StationService implements IStationService {
	
	@Autowired
	private IStationDao stationDao;

	@Override
	public List<StationVO> findAllByIsActiveTrue() {
		return this.stationDao.findAllByIsActiveTrue().stream().map(Station::stationToStationVO).toList();
	}

	@Override
	public void addOrUpdate(StationVO stationVO) {
		this.stationDao.save(stationVO.stationVOToStation());
	}

	@Override
	public StationVO findById(Integer id) {
		return findStationById(id).stationToStationVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		Station station = this.findStationById(id);
		station.setActive(false);
		this.stationDao.save(station);
	}
	
	
	private Station findStationById(Integer id) {
		return this.stationDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Station not found"));
	}
	

}
