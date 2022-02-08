package com.auction.properties;

import java.util.List;

public interface IAuctionItemProprtiesService {
	
	void save(AuctionItemProprtiesVO auctionItemProprtiesVO);
	
	List<AuctionItemProprtiesVO> findAllUnSoldProperties(Long organizationItemId);
	
	List<AuctionItemProprtiesVO> findAllSoldProperties(Long organizationItemId);
	
	void updateStatus(Long id, boolean status);

}
