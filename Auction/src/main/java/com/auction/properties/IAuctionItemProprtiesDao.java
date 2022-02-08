package com.auction.properties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.organization.item.OrganizationItem;

@Repository
public interface IAuctionItemProprtiesDao extends JpaRepository<AuctionItemProprties, Long> {
	
	List<AuctionItemProprties> findAllByOrganizationItemAndIsSold(OrganizationItem organizationItem, boolean isSold);

}
