package com.auction.preparation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Tuple;

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
	
	@Query(value ="select ap.* from auction_preparation ap join admin_live_bidding_access alba on ap.id = alba.auction where alba.user = ?1 and ap.auction_status = ?2 and ap.auction_start_date_time <= ?3", nativeQuery = true)
	List<AuctionPreparation> findAllLiveAuctionOnAdmin(Long userId, String status, LocalDateTime currentDateTime);
	
	@Query(value ="select ap.* from auction_preparation ap join admin_live_bidding_access alba on ap.id = alba.auction where alba.user = ?1 and ap.auction_status = ?2 and ap.auction_start_date_time <= ?3 and ap.id in(?4)", nativeQuery = true)
	List<AuctionPreparation> findAllLiveAuctionOnAdmin(Long userId, String status, LocalDateTime currentDateTime, List<Long> auctionsId);
	
	@Query("select a.auctionDocument from AuctionPreparation a where a.id = ?1")
	String findAuctionDocumentById(Long auctionId);
	
	@Query("select a.noticeDocument from AuctionPreparation a where a.id = ?1")
	String findNoticeDocumentById(Long auctionId);
	
	@Query(value="select a.description as description, a.auctionStartDateTime as startDateTime, a.auctionEndDateTime as endDateTime, ai.reservedPrice as basePrice,ai.modifierValue as incrementValue from AuctionPreparation a join a.auctionItems ai where a.id = ?1")
	List<Tuple> findAuctionDetailForReport(Long auctionId);
	
	@Query("select am.method  from AuctionPreparation a join a.auctionMethod am where a.id = ?1")
	String findAuctionMethodById(Long auctionId);
	
	@Query("select a.auctionName from AuctionPreparation a where a.id = ?1")
	String findAuctionNameById(Long auctionId);
	

}
