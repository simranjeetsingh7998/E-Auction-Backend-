package com.auction.organization.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizationItemDao extends JpaRepository<OrganizationItem, Long> {
	
	List<OrganizationItem> findAllByItemLabelMasterIdAndIsActiveTrue(Integer itemLabelMasterId);

}
