package com.auction.user;

import lombok.Data;

@Data
public class RoleVO {

	private int id;
	private String role;
	private boolean enabled;

	public Role roleVOToRole() {
		Role role = new Role();
		role.setId(id);
		role.setRole(this.role);
		role.setEnabled(enabled);
		return role;
	}

}
