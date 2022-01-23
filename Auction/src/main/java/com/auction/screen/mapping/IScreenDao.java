package com.auction.screen.mapping;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auction.user.Role;

@Repository
public interface IScreenDao extends JpaRepository<Screen, Integer> {
           
	List<Screen> findAllByScreenRoleMappings_Role(Role role);
	
}
