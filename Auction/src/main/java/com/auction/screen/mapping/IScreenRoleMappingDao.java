package com.auction.screen.mapping;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auction.user.Role;

@Repository
public interface IScreenRoleMappingDao extends JpaRepository<ScreenRoleMapping, Integer> {
	
	@Query(value = "select new com.auction.screen.mapping.ScreenRole(srm.id, srm.screen.id) from ScreenRoleMapping as srm where srm.role =?1")
	List<ScreenRole> findAllByRole(Role role);
	
	@Modifying
	@Query(value = "delete from screen_role where id in (:ids)", nativeQuery = true)
	void deleteAllByIdIn(@Param("ids") String ids);

}
