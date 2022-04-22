package com.auction.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService implements IUserLoginService {
	
	@Autowired
	private IUserLoginDao userLoginDao;
	
	@Override
	public void addUniqueLoginTokenForUser(UserLogin userLogin) {
		this.userLoginDao.save(userLogin);
	}
	
	@Override
	public UserLogin findLastUnqiueTokenForUser(Long userId) {
		UserLogin userLogin = null;
		Optional<UserLogin> optional = this.userLoginDao.findByUserId(userId, PageRequest.of(0, 1, Sort.Direction.DESC,"id"));
		if(optional.isPresent())
			 return optional.get();
		return userLogin;
	}
	
	

}
