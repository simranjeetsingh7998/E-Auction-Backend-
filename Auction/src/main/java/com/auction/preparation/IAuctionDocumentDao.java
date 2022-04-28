package com.auction.preparation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionDocumentDao extends JpaRepository<AuctionDocument, Long> {
	
	List<AuctionDocument> findAllByIsShowTrue();

}
