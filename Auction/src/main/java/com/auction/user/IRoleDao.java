package com.auction.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.organization.Organization;

@Repository
public interface IRoleDao extends JpaRepository<Role, Integer> {
	
	Role findByRole(String role);
	
	Optional<Role> findByIdAndOrganization(Integer id, Organization organization);
	
	List<Role> findAllByOrganizationOrOrganizationIsNull(Organization organization);

}
