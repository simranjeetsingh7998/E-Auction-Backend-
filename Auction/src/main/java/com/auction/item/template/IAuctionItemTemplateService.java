package com.auction.item.template;

import java.util.List;

import com.auction.organization.Organization;

public interface IAuctionItemTemplateService {
	
	void save(AuctionItemTemplateVO auctionItemTemplateVO);
	
	boolean existByTemplateNameAndOrganizationId(String templateName, Integer organizationId);
	
	List<AuctionItemTemplateVO> findAllByOrganization();	
	
	void deActivateById(Integer id);
	
	boolean existsByIdAndOrganizationAndIsActiveTrue(Integer id, Organization organization);

}
