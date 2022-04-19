package com.auction.emd.fee.payment.mode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.preparation.AuctionPreparation;

@Repository
public interface IEMDFeePaymentModeDao extends JpaRepository<EMDFeePaymentMode, Integer> {
	
	List<EMDFeePaymentMode> findAllByIsActiveTrue();
	
	List<EMDFeePaymentMode> findAllByAuctionPreparations(AuctionPreparation auctionPreparation);

}
