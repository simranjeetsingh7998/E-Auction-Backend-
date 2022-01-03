package com.auction.event.processing.fee.mode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class EventProcessingFeeModeService implements IEventProcessingFeeModeService {
	
	@Autowired
	private IEventProcessingFeeModeDao eventProcessingFeeModeDao;

	@Override
	public List<EventProcessingFeeModeVO> findAllByIsActiveTrue() {
		return this.eventProcessingFeeModeDao.findAllByIsActiveTrue().stream().map(EventProcessingFeeMode::eventProcessingFeeModeToEventProcessingFeeModeVO).toList();
	}

	@Override
	public void addOrUpdate(EventProcessingFeeModeVO eventProcessingFeeModeVO) {
		this.eventProcessingFeeModeDao.save(eventProcessingFeeModeVO.eventProcessingFeeModeVOToEventProcessingFeeMode());
	}

	@Override
	public EventProcessingFeeModeVO findById(Integer id) {
		return findEventProcessingFeeModeById(id).eventProcessingFeeModeToEventProcessingFeeModeVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		EventProcessingFeeMode eventProcessingFeeMode = this.findEventProcessingFeeModeById(id);
		eventProcessingFeeMode.setActive(false);
		this.eventProcessingFeeModeDao.save(eventProcessingFeeMode);
	}
	
	
	private EventProcessingFeeMode findEventProcessingFeeModeById(Integer id) {
		return this.eventProcessingFeeModeDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Event Processing Fee Mode not found"));
	}
	

}
