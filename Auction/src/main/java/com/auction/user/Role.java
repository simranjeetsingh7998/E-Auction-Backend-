package com.auction.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(of = { "id" })
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5012624444472582701L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String role;

	@ColumnDefault("true")
	private boolean enabled;

	@OneToMany(mappedBy = "role", cascade = CascadeType.MERGE, orphanRemoval = true)
	private Set<User> users = new HashSet<>();

	public RoleVO roleToRoleVO() {
		RoleVO roleVO = new RoleVO();
		roleVO.setId(id);
		roleVO.setRole(role);
		roleVO.setEnabled(enabled);
		return roleVO;
	}

}
