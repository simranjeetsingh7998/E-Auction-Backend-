package com.auction.user;

public interface IUserLoginService {
	
	void addUniqueLoginTokenForUser(UserLogin userLogin);
	
	UserLogin findLastUnqiueTokenForUser(Long userId);

}
