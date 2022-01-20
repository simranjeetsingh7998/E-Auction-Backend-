package com.auction.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.auction.security.UserDetailImpl;

public class LoggedInUser {
	
	private LoggedInUser() {}

	public static final UserDetailImpl getLoggedInUserDetails() {
		 return (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
