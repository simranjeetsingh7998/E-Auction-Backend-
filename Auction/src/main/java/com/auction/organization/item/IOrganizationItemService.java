package com.auction.organization.item;

import java.util.List;

public interface IOrganizationItemService {
	
	void addOrUpdate(OrganizationItemVO organizationItemVO);
	
	OrganizationItemVO findById(Long id);
	
	void deActivate(Long id);
	
	List<OrganizationItemVO> findAllByItemLabelMasterId(Integer itemLabelMasterId);
	
	List<OrganizationItemVO> findAllByItemLabelMasterAndItemId(Integer itemLabelMasterId, Long itemId);

}
