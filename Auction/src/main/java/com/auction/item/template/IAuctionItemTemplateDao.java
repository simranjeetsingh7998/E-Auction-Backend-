package com.auction.item.template;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.organization.Organization;

@Repository
public interface IAuctionItemTemplateDao extends JpaRepository<AuctionItemTemplate, Integer> {
	
   boolean existsBytNameAndOrganizationId(String templateName, Integer organizationId);
   
   List<AuctionItemTemplate> findByOrganizationAndIsActiveTrue(Organization organization);
   
   boolean existsByIdAndOrganizationAndIsActiveTrue(Integer id, Organization organization);

  

}
