package com.auction.organization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;

@Service
public class OrganizationService implements IOrganizationService {
	
	@Autowired
	private IOrganizationDao organizationDao;

	@Override
	public List<OrganizationVO> findAllByIsActiveTrue() {
		return this.organizationDao.findAllByIsActiveTrue().stream().map(Organization::organizationToOrganizationVO).toList();
	}

	@Override
	public void addOrUpdate(OrganizationVO organizationVO) {
		this.organizationDao.save(organizationVO.organizationVOToOrganization());
	}

	@Override
	public OrganizationVO findById(Integer id) {
		return findOrganizationById(id).organizationToOrganizationVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		Organization organization = this.findOrganizationById(id);
		organization.setActive(false);
		this.organizationDao.save(organization);
	}
	
	
	private Organization findOrganizationById(Integer id) {
		return this.organizationDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Organization not found"));
	}
	

}
