package com.auction.preparation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionExtendedHistoryDao extends JpaRepository<AuctionExtendedHistory, Long> {
	
	@Query("select aeh.extendCount from AuctionExtendedHistory aeh where aeh.auctionPreparation = ?1 and aeh.round=?2")
	Integer findByAuctionPreparationAndRound(Long auctionPreparation, int round, PageRequest pageRequest);
	
	long countByAuctionPreparationAndRound(Long auctionPreparation, int round);

}
