package com.auction.item.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.global.exception.ResourceAlreadyExist;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.organization.Organization;
import com.auction.util.LoggedInUser;

@Service
public class AuctionItemTemplateService implements IAuctionItemTemplateService {
	
	@Autowired
	private IAuctionItemTemplateDao auctionItemTemplateDao;
	
	@Override
	public void save(AuctionItemTemplateVO auctionItemTemplateVO) {
		Organization organization =  LoggedInUser.getLoggedInUserDetails().getOrganization();
		String templateName = auctionItemTemplateVO.getTName();
		if(this.existByTemplateNameAndOrganizationId(templateName, organization.getId())) {
			throw new ResourceAlreadyExist(templateName+" already exists for "+organization.getOrgName()+". Please try with different name");
		}
		AuctionItemTemplate auctionItemTemplate = auctionItemTemplateVO.auctionItemTemplateVOToAuctionItemTemplate();
		auctionItemTemplate.setOrganization(organization);
		this.auctionItemTemplateDao.save(auctionItemTemplate);
	}
	
	@Override
	public boolean existByTemplateNameAndOrganizationId(String templateName, Integer organizationId) {
		return this.auctionItemTemplateDao.existsBytNameAndOrganizationId(templateName, organizationId);
	}
	
	@Override
	public List<AuctionItemTemplateVO> findAllByOrganization() {
		return this.auctionItemTemplateDao.findByOrganizationAndIsActiveTrue(LoggedInUser.getLoggedInUserDetails().getOrganization())
		.stream().map(AuctionItemTemplate::auctionItemTemplateToAuctionItemTemplateVO).toList();
	}
	
	@Override
	public void deActivateById(Integer id) {
	    AuctionItemTemplate auctionItemTemplate =  this.auctionItemTemplateDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Template not found"));
	    auctionItemTemplate.setActive(false);
	    this.auctionItemTemplateDao.save(auctionItemTemplate);
	}
	
	@Override
	public boolean existsByIdAndOrganizationAndIsActiveTrue(Integer id, Organization organization) {
		return this.auctionItemTemplateDao.existsByIdAndOrganizationAndIsActiveTrue(id, organization);
	}

}
