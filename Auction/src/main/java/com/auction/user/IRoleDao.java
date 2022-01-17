package com.auction.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends JpaRepository<Role, Integer> {
	
	Role findByRole(String role);

}
