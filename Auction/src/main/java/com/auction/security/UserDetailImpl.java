package com.auction.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.auction.organization.Organization;
import com.auction.user.User;

public class UserDetailImpl implements UserDetails {

	private User user = null;

	public UserDetailImpl(User user) {
		this.user = user;
	}

	private static final long serialVersionUID = 2260334639827445449L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getUserGrantedAuthority();
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return  (Objects.isNull(user.getEmail()) || user.getEmail().isBlank()) ? user.getMobileNumber() : user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isActive() && this.user.getRole().isEnabled();
	}
	
	public User getUser() {
		return this.user;
	}
	
	public Long getId() {
		return this.user.getId();
	}
	
	public Integer getOrganizationId() {
		return this.getOrganization().getId();
	}
	
	public Organization getOrganization() {
		return this.user.getOrganization();
	}

}
