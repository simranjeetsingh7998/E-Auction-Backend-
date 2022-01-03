package com.auction.user;

import lombok.Data;

@Data
public class RoleVO {

	private int id;
	private String role;
	private boolean enabled;

	public Role roleVOToRole() {
		Role rolee = new Role();
		rolee.setId(id);
		rolee.setRole(this.role);
		rolee.setEnabled(enabled);
		return rolee;
	}

}
