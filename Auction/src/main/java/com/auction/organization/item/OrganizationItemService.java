package com.auction.organization.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.organization.item.label.master.IItemLabelMasterDao;
import com.auction.organization.item.label.master.ItemLabelMaster;
import com.auction.util.LoggedInUser;

@Service
public class OrganizationItemService implements IOrganizationItemService {
	
	@Autowired
	private IOrganizationItemDao organizationItemDao;
	
	@Autowired
	private IItemLabelMasterDao itemLabelMasterDao;


	@Override
	public void addOrUpdate(OrganizationItemVO organizationItemVO) {
		OrganizationItem organizationItem = organizationItemVO.organizationItemVOToOrganizationItem();
		organizationItem.setItemLabelMaster(organizationItemVO.getItemLabelMasterVO().itemLabelMasterVOToItemLabelMaster());
		this.organizationItemDao.save(organizationItem);
	}
	
	@Override
	public List<OrganizationItemVO> findAllByItemLabelMasterId(Integer itemLabelMasterId) {
		return this.organizationItemDao.findAllByItemLabelMasterIdAndIsActiveTrue(itemLabelMasterId)
				.stream().map(OrganizationItem::organizationItemToOrganizationItemVO).toList();
	}
	
	@Override
	public List<OrganizationItemVO> findAllByItemLabelMasterAndItemId(Integer itemLabelMasterId, Long itemId) {
	     if(this.itemLabelMasterDao.existsByIdAndOrganization(itemLabelMasterId, LoggedInUser.getLoggedInUserDetails().getOrganization())) {
	    	 ItemLabelMaster itemLabelMaster = new ItemLabelMaster();
	    	 itemLabelMaster.setId(itemLabelMasterId);
	    	 return this.organizationItemDao.findByItemLabelMasterAndItemId(itemLabelMaster, itemId).stream()
	    			 .map(OrganizationItem::organizationItemToOrganizationItemVO).toList();
	     }
	     throw new DataMisMatchException("Label is not from your organization");
	
	}

	@Override
	public OrganizationItemVO findById(Long id) {
		return findOrganizationItemById(id).organizationItemToOrganizationItemVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Long id) {
		OrganizationItem organizationItem = this.findOrganizationItemById(id);
		organizationItem.setActive(false);
		this.organizationItemDao.save(organizationItem);
	}
	
	private OrganizationItem findOrganizationItemById(Long id) {
		return this.organizationItemDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Organization Item not found"));
	}
	

}
