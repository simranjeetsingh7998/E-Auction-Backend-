package com.auction.division;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class DivisionService implements IDivisionService {
	
	@Autowired
	private IDivisionDao divisionDao;

	@Override
	public List<DivisionVO> findAllByIsActiveTrue() {
		return this.divisionDao.findAllByIsActiveTrue().stream().map(Division::divisionToDivisionVO).toList();
	}

	@Override
	public void addOrUpdate(DivisionVO divisionVO) {
		this.divisionDao.save(divisionVO.divisionVOToDivision());
	}

	@Override
	public DivisionVO findById(Integer id) {
		return findDivisionById(id).divisionToDivisionVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		Division division = this.findDivisionById(id);
		division.setActive(false);
		this.divisionDao.save(division);
	}
	
	
	private Division findDivisionById(Integer id) {
		return this.divisionDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Division not found"));
	}
	

}
