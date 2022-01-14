package com.auction.organization.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.organization.item.label.master.IItemLabelMasterDao;
import com.auction.organization.item.label.master.ItemLabelMaster;

@Service
public class OrganizationItemService implements IOrganizationItemService {
	
	@Autowired
	private IOrganizationItemDao organizationItemDao;


	@Override
	public void addOrUpdate(OrganizationItemVO organizationItemVO) {
		OrganizationItem organizationItem = organizationItemVO.organizationItemVOToOrganizationItem();
		organizationItem.setItemLabelMaster(organizationItemVO.getItemLabelMasterVO().itemLabelMasterVOToItemLabelMaster());
		this.organizationItemDao.save(organizationItem);
	}

	@Override
	public OrganizationItemVO findById(Integer id) {
		return findOrganizationItemById(id).organizationItemToOrganizationItemVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		OrganizationItem organizationItem = this.findOrganizationItemById(id);
		organizationItem.setActive(false);
		this.organizationItemDao.save(organizationItem);
	}
	
	
	private OrganizationItem findOrganizationItemById(Integer id) {
		return this.organizationItemDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Organization Item not found"));
	}
	

}
