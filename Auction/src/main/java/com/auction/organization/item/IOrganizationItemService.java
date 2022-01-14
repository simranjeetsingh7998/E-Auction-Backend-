package com.auction.organization.item;

import java.util.List;

public interface IOrganizationItemService {
	
	void addOrUpdate(OrganizationItemVO organizationItemVO);
	
	OrganizationItemVO findById(Integer id);
	
	void deActivate(Integer id);

}
