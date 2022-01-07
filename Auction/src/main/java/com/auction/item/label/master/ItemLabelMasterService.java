package com.auction.item.label.master;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		this.itemLabelMasterDao.save(itemLabelMasterVO.itemLabelMasterVOToItemLabelMaster());
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
	
	
	private ItemLabelMaster findItemLabelMasterById(Integer id) {
		return this.itemLabelMasterDao.findById(id).orElseThrow(()->
		new ResourceNotFoundException("Item Label not found"));
	}
	

}
