package com.auction.preparation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLiveBiddingAccessDao extends JpaRepository<AdminLiveBiddingAccess, Long> {

}
