package com.auction.util;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuctionConfigDao extends JpaRepository<AuctionConfig, Integer> {

	Optional<AuctionConfig> findByConfigKey(String key);
}
