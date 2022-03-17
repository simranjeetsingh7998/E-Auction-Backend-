package com.auction.preparation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.organization.item.OrganizationItem;
import com.auction.properties.PropertiesStatus;

@Repository
public interface IPropertiesDao extends JpaRepository<Properties, Long> {
	
	long countByAuctionPreparationAndAuctionItemProprties_OrganizationItemAndAuctionItemProprties_PropertiesStatusAndAuctionItemProprties_IsActiveTrue
	(AuctionPreparation auctionPreparation, OrganizationItem organizationItem, PropertiesStatus propertiesStatus);
	
	long countByAuctionPreparationAndAuctionItemProprties_OrganizationItemAndAuctionItemProprties_IsActiveTrue
	(AuctionPreparation auctionPreparation, OrganizationItem organizationItem);

}
