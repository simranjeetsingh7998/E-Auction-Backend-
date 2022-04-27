
package com.auction.user;

import java.util.List;

public interface IRoleService {

	RoleVO findByRole(String role);
	
	void save(RoleVO roleVO);
	
	void deleteRole(Integer id);
	
	List<RoleVO> findAllByOrganization();
	
}
