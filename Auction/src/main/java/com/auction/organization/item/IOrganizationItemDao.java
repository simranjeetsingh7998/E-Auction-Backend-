package com.auction.organization.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auction.organization.item.label.master.ItemLabelMaster;

@Repository
public interface IOrganizationItemDao extends JpaRepository<OrganizationItem, Long> {
	
	List<OrganizationItem> findAllByItemLabelMasterIdAndIsActiveTrue(Integer itemLabelMasterId);
	
	List<OrganizationItem> findByItemLabelMasterAndItemId(ItemLabelMaster itemLabelMaster, Long itemId);
	
	@Query("from OrganizationItem oi join fetch oi.itemLabelMaster where oi.id = ?1")
	OrganizationItem getWithItemLabelMasterById(Long id);
	
	@Query("select oi.id from OrganizationItem oi where oi.itemValue = ?1 and oi.itemLabelMaster =?2 and isActive = true")
	Optional<Long> findByItemValueAndItemLabelMaster(String itemValue, ItemLabelMaster itemLabelMaster);

}
