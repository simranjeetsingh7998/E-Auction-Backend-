package com.auction.sector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class SectorService implements ISectorService {
	
	@Autowired
	private ISectorDao sectorDao;

	@Override
	public List<SectorVO> findAllByIsActiveTrue() {
		return this.sectorDao.findAllByIsActiveTrue().stream().map(Sector::sectorToSectorVO).toList();
	}

	@Override
	public void addOrUpdate(SectorVO sectorVO) {
		this.sectorDao.save(sectorVO.sectorVOToSector());
	}

	@Override
	public SectorVO findById(Integer id) {
		return findSectorById(id).sectorToSectorVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		Sector sector = this.findSectorById(id);
		sector.setActive(false);
		this.sectorDao.save(sector);
	}
	
	
	private Sector findSectorById(Integer id) {
		return this.sectorDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Sector not found"));
	}
	

}
