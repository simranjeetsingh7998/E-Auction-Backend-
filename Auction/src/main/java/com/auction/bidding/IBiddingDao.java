package com.auction.bidding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBiddingDao extends JpaRepository<Bidding, Long> {

}
