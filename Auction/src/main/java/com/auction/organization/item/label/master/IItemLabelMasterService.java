package com.auction.organization.item.label.master;

import java.util.List;

public interface IItemLabelMasterService {
	
	List<ItemLabelMasterVO> findAllByIsActiveTrue();
	
	void addOrUpdate(ItemLabelMasterVO itemLabelMasterVO);
	
	ItemLabelMasterVO findById(Integer id);
	
	void deActivate(Integer id);

}
