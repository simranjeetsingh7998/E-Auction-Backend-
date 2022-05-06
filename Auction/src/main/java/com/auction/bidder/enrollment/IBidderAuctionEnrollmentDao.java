package com.auction.bidder.enrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;
import com.auction.user.User;

@Repository
public interface IBidderAuctionEnrollmentDao extends JpaRepository<BidderAuctionEnrollment, Long> {

	List<BidderAuctionEnrollment> findAllByAuctionPreparation(AuctionPreparation auctionPreparation);
	
	@Query("select b.emdLimit from BidderAuctionEnrollment b where b.auctionPreparation=?1 and b.user =?2")
	Integer findRoundParticipantCount(AuctionPreparation auctionPreparation, User participant);
	
	@Query("select sum(b.emdLimit) from BidderAuctionEnrollment b where b.auctionPreparation=?1")
	Integer totalEmdCountByAuctionPreparationId(AuctionPreparation auctionPreparation);
	
	Long countByAuctionPreparation(AuctionPreparation auctionPreparation);
	
	boolean existsByAuctionPreparationAndUser(AuctionPreparation auctionPreparation, User participant);
	
	Optional<BidderAuctionEnrollment> findByAuctionPreparationAndUser(AuctionPreparation auctionPreparation, User participant);

	void save(BidderAuctionEnrollmentVO bidder);

	@Query(value = "Select * from bidder_auction_enrollment where auction_preparation_id=?1", nativeQuery= true)
	List<BidderAuctionEnrollment> findAllByAuctionPreparationId(Long auctionPreparationId);

	Optional<BidderAuctionEnrollment> findByAuctionPreparationIdAndId(Long auctionId, Long bidderEnrollmentId);
	
	@Query("select bae.addressProof from BidderAuctionEnrollment bae where bae.user.id = ?2 and bae.auctionPreparation.id =?1")
	String findAddressProofByParticipantId(Long auctionId, Long userId);
	
	@Query("select bae.transactionProof from BidderAuctionEnrollment bae where bae.user.id = ?2 and bae.auctionPreparation.id =?1")
	String findTransactionProofByParticipantId(Long auctionId, Long userId);
	
}
