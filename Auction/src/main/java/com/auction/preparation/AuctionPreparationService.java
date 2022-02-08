package com.auction.preparation;

import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.item.template.AuctionItemTemplate;
import com.auction.item.template.IAuctionItemTemplateDao;
import com.auction.organization.Organization;
import com.auction.organization.item.IOrganizationItemDao;
import com.auction.organization.item.OrganizationItem;
import com.auction.organization.item.OrganizationItemVO;
import com.auction.property.type.PropertyType;
import com.auction.property.type.PropertyTypeVO;
import com.auction.util.FileUpload;
import com.auction.util.LoggedInUser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuctionPreparationService implements IAuctionPreparationService {
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Autowired 
	private IAuctionItemDao auctionItemDao;
	
	@Autowired
	private IAuctionItemTemplateDao auctionItemTemplateDao;
	
	@Autowired 
	private IReturnReasonDao returnReasonDao;
	
	@Autowired
	private IOrganizationItemDao organizationItemDao;
	
	@Autowired
	private FileUpload fileUpload;
	
	private static final String AUCTIONNOTFOUND = "Auction not found";
	
	@Transactional
	@Override
	public AuctionPreparationVO save(AuctionPreparationVO auctionPreparationVO) {
		AuctionPreparation auctionPreparation = auctionPreparationVO.auctionPreparationVOToAuctionPreparation();
		// adding auction item to auction preparation
//		  for(AuctionItemVO auctionItemVO : auctionPreparationVO.getAuctionItems()) {
//			  AuctionItem auctionItem = auctionItemVO.auctionItemVOToAuctionItem();
//			  auctionItem.setOrganizationItem(auctionItemVO.getOrganizationItem().organizationItemVOToOrganizationItem());
//			  auctionPreparation.addAuctionItem(auctionItem);
//		}
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
        auctionPreparation.setCreatedBy(LoggedInUser.getLoggedInUserDetails().getUser());
        Organization organization =	LoggedInUser.getLoggedInUserDetails().getOrganization();	
        LocalDate localDate = createdAt.atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        auctionPreparation.setAuctionName(generateAuctionName(organization.getOrgName(),
        		getCountOfMonth(month, year, createdAt, organization.getId()), month));
        this.auctionPreparationDao.save(auctionPreparation);
        auctionPreparationVO.setAuctionName(auctionPreparation.getAuctionName());
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
	public AuctionItemVO addAuctionItem(Long auctionPreparationId, String auctionItemString,
			MultipartFile multipartFile) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		AuctionItemVO auctionItemVO = objectMapper.readValue(auctionItemString, AuctionItemVO.class);
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(auctionPreparationId).orElseThrow(() 
				-> new ResourceNotFoundException(AUCTIONNOTFOUND));
	 if(!Objects.isNull(multipartFile)) {
		StringBuilder directory = this.fileUpload.getDirectory(FileUpload.AUCTIONDIRECTORY, ""+auctionPreparationId, "items"); 
		StringBuilder fileName = new StringBuilder(UUID.randomUUID().toString());
		String fileOriginalName = multipartFile.getOriginalFilename();
		fileName.append(fileOriginalName.substring(fileOriginalName.lastIndexOf(".")));
		this.fileUpload.uploadMultipartDocument(
				directory.toString(),fileName.toString(), multipartFile);
		auctionItemVO.setItemDocument(directory.append(File.separator).append(fileName.toString()).toString());
	   }
		AuctionItem auctionItem = auctionItemVO.auctionItemVOToAuctionItem();
		auctionItem.setAuctionPreparation(auctionPreparation);
	   if(!Objects.isNull(auctionItemVO.getOrganizationItem())) {
		   auctionItem.setOrganizationItem(this.organizationItemDao.getById(auctionItemVO.getOrganizationItem().getId()));
	   }
		return this.auctionItemDao.save(auctionItem).auctionItemToAuctionItemVO();
	}
	
	@Override
	public Map<Integer, OrganizationItemVO> findOrganizationItemsByAuctionIdAndItemId(Long auctionId, Long itemId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionId);
		AuctionItem auctionItem = this.auctionItemDao.findByIdAndAuctionPreparation(itemId, auctionPreparation).orElseThrow(() -> 
		new ResourceNotFoundException("Auction item not found"));
		OrganizationItem organizationItem = auctionItem.getOrganizationItem();
		Long organizationItemParentItemId = organizationItem.getItemId();
        Map<Integer, OrganizationItemVO> organizationItemByLabelIdMap = new HashMap<>();
        organizationItemByLabelIdMap.put(organizationItem.getItemLabelMaster().getId(), organizationItem.organizationItemToOrganizationItemVO());
		while (!Objects.isNull(organizationItemParentItemId)) {
			 OrganizationItem item =  this.organizationItemDao.getWithItemLabelMasterById(organizationItemParentItemId);
			 organizationItemByLabelIdMap.put(item.getItemLabelMaster().getId(), item.organizationItemToOrganizationItemVO());
			 organizationItemParentItemId = item.getItemId();
		}
		return organizationItemByLabelIdMap;
	}
	
	@Transactional
	@Override
	public void deleteAuctionItem(Long auctionPreparationId, Long auctionItemId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionPreparationId);
		this.auctionItemDao.deleteByIdAndAuctionPreparation(auctionItemId, auctionPreparation);
	}
	
	@Override
	public Map<String, String> uploadDocument(Long auctionPreparationId, String documentType,
			MultipartFile multipartFile) throws IOException {
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(auctionPreparationId).orElseThrow(()
				-> new ResourceNotFoundException(AUCTIONNOTFOUND));
		StringBuilder directory = new StringBuilder();
		directory.append(FileUpload.AUCTIONDIRECTORY);
		directory.append(File.separator);
		directory.append(auctionPreparationId);
		StringBuilder fileName = new StringBuilder(documentType);
		String fileOriginalName = multipartFile.getOriginalFilename();
		fileName.append(fileOriginalName.substring(fileOriginalName.lastIndexOf(".")));
		Map<String, String> responseMap = new HashMap<>();
		if(AuctionDocumentType.NOTICE.name().equalsIgnoreCase(documentType)) {
			 this.fileUpload.uploadMultipartDocument(directory.toString(), fileName.toString(), multipartFile);
			 auctionPreparation.setNoticeDocument(directory.append(File.separator).toString()+fileName.toString());
			 responseMap.put("path", auctionPreparation.getNoticeDocument());
		} else if(AuctionDocumentType.AUCTION.name().equalsIgnoreCase(documentType)) {
			 this.fileUpload.uploadMultipartDocument(directory.toString(), fileName.toString(), multipartFile);
			 auctionPreparation.setAuctionDocument(directory.append(File.separator).toString()+fileName.toString());
			 responseMap.put("path", auctionPreparation.getAuctionDocument());
		}
		this.auctionPreparationDao.save(auctionPreparation);
		return responseMap;
	}
	
	@Override
	public void mapToTemplate(Long id, Integer templateId) {
          AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(id).orElseThrow(() ->
          new ResourceNotFoundException(AUCTIONNOTFOUND));
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
	public void publish(Long id, AuctionPreparationVO auctionPreparationVO) {
	   AuctionPreparation auctionPreparation  =	this.auctionPreparationDao.findById(id).orElseThrow(
			   () -> new ResourceNotFoundException(AUCTIONNOTFOUND));
	   if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.APPROVE.getStatus())) {
		   auctionPreparation.setAuctionStatus(AuctionStatus.PUBLISH);
		   auctionPreparation.setRegistrationStartDateTime(auctionPreparationVO.getRegistrationStartDateTime());
		   auctionPreparation.setRegistrationEndDateTime(auctionPreparationVO.getRegistrationEndDateTime());
		   this.auctionPreparationDao.save(auctionPreparation);
	   } else {
		    throw new DataMisMatchException("Auction can't be published");
	   }
	}
	
	@Override
	public void schedule(Long id) {
		AuctionPreparation auctionPreparation  =	this.auctionPreparationDao.findById(id).orElseThrow(
				   () -> new ResourceNotFoundException("Auction not found"));
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.PUBLISH.getStatus())) {
			
		} else {
			throw new DataMisMatchException("Auction can't be scheduled");
		}
	}

	@Override
	public void returnAuction(Long auctionPreparationId, ReturnReasonVO returnReasonVO) {
		 AuctionPreparation auctionPreparation  =	this.auctionPreparationDao.findById(auctionPreparationId)
				 .orElseThrow(() -> new ResourceNotFoundException(AUCTIONNOTFOUND));
		 if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.APPROVE.getStatus())) {
		 auctionPreparation.setAuctionStatus(AuctionStatus.DRAFT);
		 ReturnReason returnReason = returnReasonVO.returnReasonVOToReturnReason();
		 returnReason.setReturnBy(LoggedInUser.getLoggedInUserDetails().getUser());
		 auctionPreparation.addReturnReason(returnReason);
		 this.auctionPreparationDao.save(auctionPreparation);
	   }
	   else
	      throw new DataMisMatchException("Auction can't be returned");
	}
	
	@Override
	public List<ReturnReasonVO> returnReasonsByAuctionId(Long auctionId) {
		return this.returnReasonDao.findAllByAuctionPreparation(auctionId).stream().map(returnReason -> {
			  ReturnReasonVO returnReasonVO = returnReason.returnReasonToReturnReasonVO();
			  returnReasonVO.setReturnBy(returnReason.getReturnBy().userToUserVO());
			  return returnReasonVO;
		}).toList();
	}
	
	
	@Override
	public AuctionPreparationVO findAllDetailsById(Long id) {
		List<AuctionPreparation> auctionPreparations = this.auctionPreparationDao.findAll(AuctionPreparationSpecification.fullDetailsById(id));
		if(!auctionPreparations.isEmpty()) {
		    return this.checkAndAddAssociation(auctionPreparations.get(0));
		}
		throw new ResourceNotFoundException("Auction preparation not found");
	}
	
	@Override
	public List<AuctionPreparationVO> findAuctionByStatus(String status) {
		return this.auctionPreparationDao.findAllByAuctionStatus(AuctionStatus.valueOf(status))
		.stream().map(AuctionPreparation::auctionPreparationToAuctionPreparationVO).toList();
	}
	
	private AuctionPreparationVO checkAndAddAssociation(AuctionPreparation auctionPreparation) {
		 AuctionPreparationVO auctionPreparationVO = auctionPreparation.auctionPreparationToAuctionPreparationVO();
		    if(!Objects.isNull(auctionPreparation.getAuctionType()))
		    	    auctionPreparationVO.setAuctionType(auctionPreparation.getAuctionType().auctionTypeToAuctionTypeVO());
		    if(!Objects.isNull(auctionPreparation.getAuctionMethod()))
		    	     auctionPreparationVO.setAuctionMethod(auctionPreparation.getAuctionMethod().auctionMethodToAuctionMethodVO());
		    if(!Objects.isNull(auctionPreparation.getAuctionItemTemplate()))
		    	auctionPreparationVO.setAuctionItemTemplateVO(auctionPreparation.getAuctionItemTemplate().auctionItemTemplateToAuctionItemTemplateVO());
		    if(!Objects.isNull(auctionPreparation.getAuctionProcess()))
		    	auctionPreparationVO.setAuctionProcess(auctionPreparation.getAuctionProcess().auctionProcessToAuctionProcessVO());
		    if(!Objects.isNull(auctionPreparation.getBidSubmissionPlacement()))
		    	 auctionPreparationVO.setBidSubmissionPlacement(auctionPreparation.getBidSubmissionPlacement().bidSubmissionPlacementToBidSubmissionPlacementVO());
		    if(!Objects.isNull(auctionPreparation.getEmdAppliedFor()))
		    	auctionPreparationVO.setEmdAppliedFor(auctionPreparation.getEmdAppliedFor().emdAppliedForToEMDAppliedForVO());
		    if(!Objects.isNull(auctionPreparation.getEmdFeePaymentMode()))
		    	auctionPreparationVO.setEmdFeePaymentMode(auctionPreparation.getEmdFeePaymentMode().emdFeePaymentModeToEMDFeePaymentModeVO());
		    if(!Objects.isNull(auctionPreparation.getEventProcessingFeeMode()))
		    	auctionPreparationVO.setEventProcessingFeeMode(auctionPreparation.getEventProcessingFeeMode().eventProcessingFeeModeToEventProcessingFeeModeVO());
		    if(!Objects.isNull(auctionPreparation.getPropertyType())) {
		    	 PropertyType propertyType = auctionPreparation.getPropertyType();
		    	 PropertyTypeVO propertyTypeVO = propertyType.propertyTypeToPropertyTypeVO();
		    	 propertyTypeVO.setAuctionCategory(propertyType.getAuctionCategory().auctionCategoryToAuctionCategoryVO());
		    	 auctionPreparationVO.setPropertyTypeVO(propertyTypeVO); 
		    }
		    if(!Objects.isNull(auctionPreparation.getAuctionItems())) {
		    	  auctionPreparationVO.setAuctionItems(auctionPreparation.getAuctionItems().stream().map(AuctionItem::auctionItemToAuctionItemVO).toList());
		    }
		  return auctionPreparationVO;  
	}
	
}
