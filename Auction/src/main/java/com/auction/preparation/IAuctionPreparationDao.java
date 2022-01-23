package com.auction.preparation;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionPreparationDao extends JpaRepository<AuctionPreparation, Long> {
	
	@Query(value="select count(auctionpre0_.id) as col_0_0_ from auction_preparation auctionpre0_ join users u on u.id=auctionpre0_.created_by"
			+ " where auctionpre0_.created_date between ?1 and ?2 and u.organization_id = ?3",
			nativeQuery = true)
	long countByCreatedDateBetween(Instant from, Instant to, Integer organizationId);

}
