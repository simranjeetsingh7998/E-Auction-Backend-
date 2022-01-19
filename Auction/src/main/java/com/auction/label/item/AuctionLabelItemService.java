package com.auction.label.item;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.global.exception.ResourceNotFoundException;
import com.auction.organization.item.IOrganizationItemDao;
import com.auction.organization.item.OrganizationItem;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.IAuctionPreparationDao;

@Service
public class AuctionLabelItemService implements IAuctionLabelItemService {
	
	@Autowired
	private IAuctionItemLabelDao auctionItemLabelDao;
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Autowired
	private IOrganizationItemDao organizationItemDao;

	@Override
	public void save(AuctionItemLabelVO auctionItemLabelVO) {
	        AuctionLabelItem auctionLabelItem = new AuctionLabelItem();
	        if(auctionItemLabelVO.getId() !=0) {
	        	 auctionLabelItem.setId(auctionItemLabelVO.getId());
	        }
	        Long auctionPreparationId = auctionItemLabelVO.getAuctionPreparationId();
	        if(!Objects.isNull(auctionPreparationId) && auctionPreparationId !=0) {
	           AuctionPreparation auctionPreparation =	this.auctionPreparationDao.findById(auctionPreparationId).orElseThrow(() -> new ResourceNotFoundException("Auction Preparation not found"));
	           auctionLabelItem.setAuctionPreparation(auctionPreparation);
	           auctionLabelItem.setLabelType(LabelType.CUSTOM.getLabelType());
	        } else {
	           auctionLabelItem.setLabelType(LabelType.STANDARD.getLabelType());
	        }
	        
	       OrganizationItem organizationItem =  this.organizationItemDao.findById(auctionItemLabelVO.getOrganizationItemId()).orElseThrow(() -> new ResourceNotFoundException("Organization Item not found"));
	       auctionLabelItem.setOrganizationItem(organizationItem);
	       auctionLabelItem.setLabelType(auctionItemLabelVO.getLabelType());
	       
	       this.auctionItemLabelDao.save(auctionLabelItem);
	}
	
}
