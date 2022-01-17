package com.auction.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {
	
	@Autowired
	private IRoleDao roleDao;
	
	@Override
	public RoleVO findByRole(String role) {
		return this.roleDao.findByRole(role).roleToRoleVO();
	}

}
