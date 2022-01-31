package com.auction.preparation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IReturnReasonDao extends JpaRepository<ReturnReason, Long> {

	@Query("from ReturnReason rr inner join fetch rr.returnBy where rr.auctionPreparation.id = ?1")
	List<ReturnReason> findAllByAuctionPreparation(Long auctionPreparationId);
	
}
