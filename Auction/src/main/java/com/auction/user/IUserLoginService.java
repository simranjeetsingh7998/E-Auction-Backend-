package com.auction.user;

public interface IUserLoginService {
	
	void addUniqueLoginTokenForUser(UserLogin userLogin);
	
	UserLogin findLastUnqiueTokenForUser(Long userId);
	
	void logoutFromOtherDevices(Long userId);
	
	void deleteUserByToken(String token);

}
