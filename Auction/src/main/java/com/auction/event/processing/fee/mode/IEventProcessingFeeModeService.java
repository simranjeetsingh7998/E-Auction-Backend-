package com.auction.event.processing.fee.mode;

import java.util.List;

public interface IEventProcessingFeeModeService {
	
	List<EventProcessingFeeModeVO> findAllByIsActiveTrue();
	
	void addOrUpdate(EventProcessingFeeModeVO eventProcessingFeeModeVO);
	
	EventProcessingFeeModeVO findById(Integer id);
	
	void deActivate(Integer id);

}
