package com.auction.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.global.exception.ResourceNotFoundException;
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
		role.setOrganization(LoggedInUser.getLoggedInUserDetails().getOrganization());
		this.roleDao.save(role);
	}
	
	@Override
	public void deleteRole(Integer id) {
		Role role = this.roleDao.findByIdAndOrganization(id, LoggedInUser.getLoggedInUserDetails().getOrganization())
		.orElseThrow(() -> new ResourceNotFoundException("Role not found"));
		role.setEnabled(false);
		this.roleDao.save(role);
	}
	
	@Override
	public List<RoleVO> findAllByOrganization() {
		return this.roleDao.findAllByOrganizationOrOrganizationIsNull(LoggedInUser.getLoggedInUserDetails().getOrganization())
				.stream().filter(Role::isEnabled).map(Role::roleToRoleVO).toList();
	}

}
