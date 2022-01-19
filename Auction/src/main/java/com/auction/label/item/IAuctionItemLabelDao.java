package com.auction.label.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionItemLabelDao extends JpaRepository<AuctionLabelItem, Long> {

}
