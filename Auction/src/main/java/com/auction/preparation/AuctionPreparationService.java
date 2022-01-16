package com.auction.preparation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionPreparationService implements IAuctionPreparationService {
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Transactional
	@Override
	public void save(AuctionPreparationVO auctionPreparationVO) {
		AuctionPreparation auctionPreparation = auctionPreparationVO.auctionPreparationVOToAuctionPreparation();
		// adding auction item to auction preparation
		auctionPreparationVO.getAuctionItems().forEach(auctionItemVO ->{
			  AuctionItem auctionItem = auctionItemVO.auctionItemVOToAuctionItem();
			  auctionItem.setOrganizationItem(auctionItemVO.getOrganizationItem().organizationItemVOToOrganizationItem());
			  auctionPreparation.addAuctionItem(auctionItem);
		});
		auctionPreparation.setAuctionType(auctionPreparationVO.getAuctionType().auctionTypeVOToAuctionType());
		auctionPreparation.setAuctionCategory(auctionPreparationVO.getAuctionCategory().auctionCategoryVOToAuctionCategory());
		auctionPreparation.setAuctionMethod(auctionPreparationVO.getAuctionMethod().auctionMethodVOToAuctionMethod());
		auctionPreparation.setAuctionProcess(auctionPreparationVO.getAuctionProcess().auctionProcessVOToAuctionProcess());
		auctionPreparation.setBidSubmissionPlacement(auctionPreparationVO.getBidSubmissionPlacement().bidSubmissionPlacementVOToBidSubmissionPlacement());
		auctionPreparation.setEmdAppliedFor(auctionPreparationVO.getEmdAppliedFor().emdAppliedForVOToEMDAppliedFor());
		auctionPreparation.setEmdFeePaymentMode(auctionPreparationVO.getEmdFeePaymentMode().emdFeePaymentModeVOToEMDFeePaymentMode());
		auctionPreparation.setEventProcessingFeeMode(auctionPreparationVO.getEventProcessingFeeMode().eventProcessingFeeModeVOToEventProcessingFeeMode());
        this.auctionPreparationDao.save(auctionPreparation);
	}
	

}
