package com.auction.item.label.template.mapping;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceAlreadyExist;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.item.template.AuctionItemTemplate;
import com.auction.item.template.IAuctionItemTemplateDao;
import com.auction.organization.item.label.master.ItemLabelMaster;
import com.auction.organization.item.label.master.ItemLabelMasterVO;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.util.LoggedInUser;

@Service
public class AuctionItemLabelTemplateMappingService implements IAuctionItemLabelTemplateMappingService {
	
	@Autowired
	private IAuctionItemLabelTemplateMappingDao auctionItemLabelTemplateMappingDao;
	
	@Autowired
	private IAuctionItemTemplateDao auctionItemTemplateDao;
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Override
	public void save(AuctionItemLabelTemplateMappingVO auctionItemLabelTemplateMappingVO) {
		Integer order =  auctionItemLabelTemplateMappingVO.getLabelOrder();
		AuctionItemTemplate auctionItemTemplate = auctionItemLabelTemplateMappingVO.getAuctionItemTemplate().auctionItemTemplateVOToAuctionItemTemplate();
		ItemLabelMaster itemLabelMaster = auctionItemLabelTemplateMappingVO.getItemLabelMaster().itemLabelMasterVOToItemLabelMaster();
		if(this.existByLabelOrderAndAuctionItemTemplateAndItemLabelMaster(order,auctionItemTemplate,itemLabelMaster)) {
			 throw new ResourceAlreadyExist("Label already exist for this template");
		} else if(this.auctionItemLabelTemplateMappingDao.existsByAuctionItemTemplateAndItemLabelMaster(auctionItemTemplate, itemLabelMaster)) {
			throw new ResourceAlreadyExist("Label already exist for this template");
		} else if(this.auctionItemLabelTemplateMappingDao.existsByLabelOrderAndAuctionItemTemplate(order, auctionItemTemplate)) {
			throw new ResourceAlreadyExist("Template already has label for this order");
		}
		else {
			  AuctionItemLabelTemplateMapping auctionItemLabelTemplateMapping  = new AuctionItemLabelTemplateMapping();
			  Integer id = auctionItemLabelTemplateMappingVO.getId();
			  if(!Objects.isNull(id) && id !=0) {
			      auctionItemLabelTemplateMapping.setId(id);
			  }
			  auctionItemLabelTemplateMapping.setLabelOrder(order);
			  auctionItemLabelTemplateMapping.setAuctionItemTemplate(auctionItemTemplate);
			  auctionItemLabelTemplateMapping.setItemLabelMaster(itemLabelMaster);
			  this.auctionItemLabelTemplateMappingDao.save(auctionItemLabelTemplateMapping);
		}
			
		
	}
	
	@Override
	public boolean existByLabelOrderAndAuctionItemTemplateAndItemLabelMaster(Integer order,
			AuctionItemTemplate auctionItemTemplate, ItemLabelMaster itemLabelMaster) {
		return this.auctionItemLabelTemplateMappingDao.existsByLabelOrderAndAuctionItemTemplateAndItemLabelMaster(order, auctionItemTemplate, itemLabelMaster);
	}
	
	@Override
	public void deleteById(Integer id) {
		this.auctionItemLabelTemplateMappingDao.deleteById(id);
	}
	
	@Transactional
	@Override
	public void deleteByTemplateIdAndLabelId(Integer templateId, Integer labelId) {
		AuctionItemTemplate auctionItemTemplate = new AuctionItemTemplate();
		auctionItemTemplate.setId(templateId);
		if(this.auctionPreparationDao.countByAuctionItemTemplate(auctionItemTemplate) > 0)
			throw new ResourceAlreadyExist("Template Mapping can't be deleted because Auction preparation is created with this template");
		ItemLabelMaster itemLabelMaster = new ItemLabelMaster();
		itemLabelMaster.setId(labelId);
		this.auctionItemLabelTemplateMappingDao.deleteByAuctionItemTemplateAndItemLabelMaster(auctionItemTemplate, itemLabelMaster);
	}
	
	@Override
	public List<ItemLabelMasterVO> findAllByAuctionItemTemplate(Integer auctionItemTemplateId) {
		AuctionItemTemplate auctionItemTemplate = new AuctionItemTemplate();
		auctionItemTemplate.setId(auctionItemTemplateId);
		if(this.auctionItemTemplateDao.existsByIdAndOrganizationAndIsActiveTrue(auctionItemTemplateId, LoggedInUser.getLoggedInUserDetails().getOrganization())) {
			return this.auctionItemLabelTemplateMappingDao.findAllByAuctionItemTemplate(auctionItemTemplate,
					Sort.by(Sort.Order.asc("labelOrder"))).stream()
					.map(mapped -> mapped.getItemLabelMaster().itemLabelMasterToItemLabelMasterVO()).toList();
		} else {
			throw new ResourceNotFoundException("This template is not created for your organization");
		}

	}

}
