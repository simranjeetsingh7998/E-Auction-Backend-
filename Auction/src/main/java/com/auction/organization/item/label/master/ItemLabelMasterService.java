package com.auction.organization.item.label.master;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.global.exception.ResourceAlreadyExist;
import com.auction.global.exception.ResourceNotFoundException;

@Service
public class ItemLabelMasterService implements IItemLabelMasterService {
	
	@Autowired
	private IItemLabelMasterDao itemLabelMasterDao;

	@Override
	public List<ItemLabelMasterVO> findAllByIsActiveTrue() {
		return this.itemLabelMasterDao.findAllByIsActiveTrue().stream().map(ItemLabelMaster::itemLabelMasterToItemLabelMasterVO).toList();
	}

	@Override
	public void addOrUpdate(ItemLabelMasterVO itemLabelMasterVO) {
		Optional<ItemLabelMaster> optionalItemLableMaster = this.itemLabelMasterDao.findByLabelOrderAndOrganizationIdAndIsActiveTrue(itemLabelMasterVO.getOrder(),
				itemLabelMasterVO.getOrganizationVO().getId());
		if(!optionalItemLableMaster.isPresent()) {
			ItemLabelMaster itemLabelMaster = itemLabelMasterVO.itemLabelMasterVOToItemLabelMaster();
			itemLabelMaster.setOrganization(itemLabelMasterVO.getOrganizationVO().organizationVOToOrganization());
	    	this.itemLabelMasterDao.save(itemLabelMaster);
		} else throw new ResourceAlreadyExist("Item Label Already exist with Order "+itemLabelMasterVO.getOrder()+ " for "+ itemLabelMasterVO.getOrganizationVO().getName());
	}

	@Override
	public ItemLabelMasterVO findById(Integer id) {
		return this.findItemLabelMasterById(id).itemLabelMasterToItemLabelMasterVO();
	}
	
	@Transactional
	@Override
	public void deActivate(Integer id) {
		ItemLabelMaster itemLabelMaster = this.findItemLabelMasterById(id);
		itemLabelMaster.setActive(false);
		this.itemLabelMasterDao.save(itemLabelMaster);
	}
	
	@Override
	public List<ItemLabelMasterVO> findAllByOrganizationId(Integer organizationId){
		return this.itemLabelMasterDao.findAllByOrganizationIdAndIsActiveTrue(organizationId, Sort.by(Sort.Direction.ASC,"labelOrder")).stream().map(ItemLabelMaster::itemLabelMasterToItemLabelMasterVO).toList();
	}
	
	private ItemLabelMaster findItemLabelMasterById(Integer id) {
		return this.itemLabelMasterDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Item Label not found"));
	}
	

}
