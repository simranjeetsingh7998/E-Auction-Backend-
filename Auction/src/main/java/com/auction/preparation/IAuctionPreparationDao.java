package com.auction.preparation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.auction.item.template.AuctionItemTemplate;
import com.auction.method.AuctionMethod;

@Repository
public interface IAuctionPreparationDao extends JpaRepository<AuctionPreparation, Long>, JpaSpecificationExecutor<AuctionPreparation> {
	
	@Query(value="select count(auctionpre0_.id) as col_0_0_ from auction_preparation auctionpre0_ join users u on u.id=auctionpre0_.created_by"
			+ " where auctionpre0_.created_date between ?1 and ?2 and u.organization_id = ?3",
			nativeQuery = true)
	long countByCreatedDateBetween(Instant from, Instant to, Integer organizationId);
	
	long countByAuctionItemTemplate(AuctionItemTemplate auctionItemTemplate);
	
	List<AuctionPreparation> findAllByAuctionStatus(AuctionStatus auctionStatus);
	
	List<AuctionPreparation> findAllByAuctionStatusAndAuctionFinishTimeBefore(AuctionStatus auctionStatus, LocalDateTime currentDateTime);
	
	List<AuctionPreparation> findAllByAuctionStatusAndRegistrationEndDateTimeAfter(AuctionStatus auctionStatus, LocalDateTime currentDateTime);

	List<AuctionPreparation> findAllByAuctionStatusAndAuctionMethod(AuctionStatus auctionStatus, AuctionMethod auctionMethod);

}
