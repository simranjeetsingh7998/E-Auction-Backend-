package com.auction.preparation;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.item.template.AuctionItemTemplate;
import com.auction.item.template.IAuctionItemTemplateDao;
import com.auction.organization.Organization;
import com.auction.util.LoggedInUser;

@Service
public class AuctionPreparationService implements IAuctionPreparationService {
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Autowired
	private IAuctionItemTemplateDao auctionItemTemplateDao;
	
	@Transactional
	@Override
	public AuctionPreparationVO save(AuctionPreparationVO auctionPreparationVO) {
		AuctionPreparation auctionPreparation = auctionPreparationVO.auctionPreparationVOToAuctionPreparation();
		// adding auction item to auction preparation
		  for(AuctionItemVO auctionItemVO : auctionPreparationVO.getAuctionItems()) {
			  AuctionItem auctionItem = auctionItemVO.auctionItemVOToAuctionItem();
			  auctionItem.setOrganizationItem(auctionItemVO.getOrganizationItem().organizationItemVOToOrganizationItem());
			  auctionPreparation.addAuctionItem(auctionItem);
		}
		if(!Objects.isNull(auctionPreparationVO.getAuctionType()))  
		        auctionPreparation.setAuctionType(auctionPreparationVO.getAuctionType().auctionTypeVOToAuctionType());
		if(!Objects.isNull(auctionPreparationVO.getAuctionMethod()))
		        auctionPreparation.setAuctionMethod(auctionPreparationVO.getAuctionMethod().auctionMethodVOToAuctionMethod());
//		if(!Objects.isNull(auctionPreparationVO.getAuctionCategory()))
//		        auctionPreparation.setAuctionCategory(auctionPreparationVO.getAuctionCategory().auctionCategoryVOToAuctionCategory());
		if(!Objects.isNull(auctionPreparationVO.getPropertyTypeVO()))
	        auctionPreparation.setPropertyType(auctionPreparationVO.getPropertyTypeVO().propertyTypeVOToPropertyType());
		if(!Objects.isNull(auctionPreparationVO.getAuctionProcess()))
		         auctionPreparation.setAuctionProcess(auctionPreparationVO.getAuctionProcess().auctionProcessVOToAuctionProcess());
		if(!Objects.isNull(auctionPreparationVO.getBidSubmissionPlacement()))
	          	auctionPreparation.setBidSubmissionPlacement(auctionPreparationVO.getBidSubmissionPlacement().bidSubmissionPlacementVOToBidSubmissionPlacement());
		if(!Objects.isNull(auctionPreparationVO.getEmdAppliedFor()))
		        auctionPreparation.setEmdAppliedFor(auctionPreparationVO.getEmdAppliedFor().emdAppliedForVOToEMDAppliedFor());
		if(!Objects.isNull(auctionPreparationVO.getEmdFeePaymentMode()))
		        auctionPreparation.setEmdFeePaymentMode(auctionPreparationVO.getEmdFeePaymentMode().emdFeePaymentModeVOToEMDFeePaymentMode());
		if(!Objects.isNull(auctionPreparationVO.getEventProcessingFeeMode()))
		        auctionPreparation.setEventProcessingFeeMode(auctionPreparationVO.getEventProcessingFeeMode().eventProcessingFeeModeVOToEventProcessingFeeMode());
        if(!Objects.isNull(auctionPreparationVO.getAuctionItemTemplateVO()))
        	    auctionPreparation.setAuctionItemTemplate(auctionPreparationVO.getAuctionItemTemplateVO().auctionItemTemplateVOToAuctionItemTemplate());
		auctionPreparation = this.auctionPreparationDao.save(auctionPreparation);
        Instant createdAt = auctionPreparation.getCreatedDate();
        if(auctionPreparation.getAuctionName() == null && createdAt !=null) {
        Organization organization =	LoggedInUser.getLoggedInUserDetails().getOrganization();	
        LocalDate localDate = createdAt.atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        auctionPreparation.setAuctionName(generateAuctionName(organization.getOrgName(),
        		getCountOfMonth(month, year, createdAt, organization.getId()), month));
        this.auctionPreparationDao.save(auctionPreparation);
       }
        auctionPreparationVO.setId(auctionPreparation.getId());
       return auctionPreparationVO; 
	}
	
	private long getCountOfMonth(int month, int year, Instant to, Integer organizationId) {
	       Instant from = LocalDateTime.of(year, month, 1, 0, 0,0).toInstant(ZoneOffset.UTC);
	       return this.auctionPreparationDao.countByCreatedDateBetween(from, to, organizationId);
	}
	
	private String generateAuctionName(String organizationName,Long count, int month) {
		return organizationName+"/"+new DateFormatSymbols().getShortMonths()[month-1]+"/"+count;
	}
	
	@Override
	public void mapToTemplate(Long id, Integer templateId) {
          AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
	      AuctionItemTemplate auctionItemTemplate = this.auctionItemTemplateDao.findById(templateId).orElseThrow(() -> new ResourceNotFoundException("Template not found"));
	      auctionPreparation.setAuctionItemTemplate(auctionItemTemplate);
	      this.auctionPreparationDao.save(auctionPreparation);
	}
	
	@Override
	public List<AuctionPreparationVO> searchAuctionPreparation(AuctionPreparationSearchParam auctionPreparationSearchParam) {
		  return this.auctionPreparationDao.findAll(AuctionPreparationSpecification.search(auctionPreparationSearchParam))
		   .stream().map(AuctionPreparation::auctionPreparationToAuctionPreparationVO).toList();
		  
	}
	
	@Override
	public List<AuctionPreparationVO> findAllDetailsById(Long id) {
		return null;
	}
	

}
