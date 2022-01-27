package com.auction.organization.item.label.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.organization.Organization;

@Repository
public interface IItemLabelMasterDao extends JpaRepository<ItemLabelMaster, Integer> {
	
	List<ItemLabelMaster> findAllByIsActiveTrue();
	
	Optional<ItemLabelMaster> findByItemLabelAndOrganizationIdAndIsActiveTrue(String itemLabel, Integer organizationId);
	
	List<ItemLabelMaster> findAllByOrganizationIdAndIsActiveTrue(Integer organizationId, Sort orderBy);
	
	boolean existsByIdAndOrganization(Integer id, Organization organization);

}
