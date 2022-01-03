package com.auction.process;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionProcessDao extends JpaRepository<AuctionProcess, Integer> {
	
	List<AuctionProcess> findAllByIsActiveTrue();

}
