package com.auction.user;

import java.util.List;
import java.util.Map;

public interface IUserService {

	UserVO createUser(UserVO userVO);

	List<UserVO> getAllUsers();

	void changePassword(Long userId, ChangePasswordVO changePasswordVO) throws Exception;

	User findUserById(Long userId);
	
	UserVO findFullUserDetailsById(Long id);
	
	void disableUserById(Long id);
	
	void sendEmailOtp(String to) throws Exception;
	
	void sendPhoneOtp(String to);
	
	void verifyEmailOtp(Map<String, String> verificationMap) throws Exception;
	
	void verifyPhoneOtp(Map<String, String> verificationMap);

}
