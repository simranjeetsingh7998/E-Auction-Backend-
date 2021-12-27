package com.auction.user;

import java.util.List;

public interface IUserService {

	UserVO createUser(UserVO userVO);

	List<UserVO> getAllUsers();

	void changePassword(Long userId, ChangePasswordVO changePasswordVO) throws Exception;

	User findUserById(Long userId);

}
