package com.auction.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.util.LoggedInUser;

@Service
public class RoleService implements IRoleService {
	
	@Autowired
	private IRoleDao roleDao;
	
	@Override
	public RoleVO findByRole(String role) {
		return this.roleDao.findByRole(role).roleToRoleVO();
	}
	
	@Override
	public void save(RoleVO roleVO) {
		Role role = roleVO.roleVOToRole();
		if(role.getId() ==0)
			 role.setOrganization(LoggedInUser.getLoggedInUserDetails().getOrganization());
		this.roleDao.save(role);
	}
	
	@Override
	public void deleteRole(Integer id) {
		this.roleDao.deleteById(id);
	}
	
	@Override
	public List<RoleVO> findAllByOrganization() {
		return this.roleDao.findAllByOrganizationOrOrganizationIsNull(LoggedInUser.getLoggedInUserDetails().getOrganization())
				.stream().map(Role::roleToRoleVO).toList();
	}

}
