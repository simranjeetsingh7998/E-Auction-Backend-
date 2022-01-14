package com.auction.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizationDao extends JpaRepository<Organization, Integer> {
	
	List<Organization> findAllByIsActiveTrue();

}
