package com.auction.emd.applied;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEMDAppliedForDao extends JpaRepository<EMDAppliedFor, Integer> {
	
	List<EMDAppliedFor> findAllByIsActiveTrue();

}
