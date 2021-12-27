package com.auction.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.auction.user.IUserDao;
import com.auction.user.User;

@Component
public class UserServiceDetailImpl implements UserDetailsService {

	@Autowired
	private IUserDao userDao;

	@Override
	public UserDetailImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userDao.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new UserDetailImpl(user);
	}

}
