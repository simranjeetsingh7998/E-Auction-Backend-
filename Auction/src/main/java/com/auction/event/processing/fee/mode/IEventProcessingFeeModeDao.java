package com.auction.event.processing.fee.mode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventProcessingFeeModeDao extends JpaRepository<EventProcessingFeeMode, Integer> {
	
	List<EventProcessingFeeMode> findAllByIsActiveTrue();

}
