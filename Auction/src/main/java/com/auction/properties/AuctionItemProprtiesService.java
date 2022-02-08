package com.auction.properties;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.organization.item.OrganizationItem;
import com.auction.organization.item.OrganizationItemVO;

@Service
public class AuctionItemProprtiesService implements IAuctionItemProprtiesService {
	
	@Autowired
	private IAuctionItemProprtiesDao auctionItemProprtiesDao;
	
	@Override
	public void save(AuctionItemProprtiesVO auctionItemProprtiesVO) {
		OrganizationItemVO organizationItemVO = auctionItemProprtiesVO.getOrganizationItem();
		AuctionItemProprties auctionItemProprties = auctionItemProprtiesVO.auctionItemProprtiesVOToAuctionItemProprties();
		if(!Objects.isNull(organizationItemVO)) {
			auctionItemProprties.setOrganizationItem(organizationItemVO.organizationItemVOToOrganizationItem());
		}
		this.auctionItemProprtiesDao.save(auctionItemProprties);
	}
	
	@Override
	public List<AuctionItemProprtiesVO> findAllSoldProperties(Long organizationItemId) {
		OrganizationItem organizationItem = new OrganizationItem();
		organizationItem.setId(organizationItemId);
		return this.auctionItemProprtiesDao.findAllByOrganizationItemAndIsSold(organizationItem, true)
				.stream().map(AuctionItemProprties::auctionItemProprtiesToAuctionItemProprtiesVO).toList();
	}
	
	@Override
	public List<AuctionItemProprtiesVO> findAllUnSoldProperties(Long organizationItemId) {
		OrganizationItem organizationItem = new OrganizationItem();
		organizationItem.setId(organizationItemId);
		return this.auctionItemProprtiesDao.findAllByOrganizationItemAndIsSold(organizationItem, false)
				.stream().map(AuctionItemProprties::auctionItemProprtiesToAuctionItemProprtiesVO).toList();
	}

}
