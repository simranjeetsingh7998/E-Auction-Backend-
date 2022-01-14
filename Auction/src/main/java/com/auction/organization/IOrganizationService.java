package com.auction.organization;

import java.util.List;

public interface IOrganizationService {
	
	List<OrganizationVO> findAllByIsActiveTrue();
	
	void addOrUpdate(OrganizationVO organizationVO);
	
	OrganizationVO findById(Integer id);
	
	void deActivate(Integer id);

}
