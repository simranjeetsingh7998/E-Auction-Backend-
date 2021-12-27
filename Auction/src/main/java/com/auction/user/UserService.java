package com.auction.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.address.Address;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;

	private final PasswordEncoder PASSWORDENCODER = new BCryptPasswordEncoder();

	@Transactional
	@Override
	public UserVO createUser(UserVO userVO) {
		User user = userVO.userVOToUser();
		user.setPassword(PASSWORDENCODER.encode(user.getPassword()));
		Role role = userVO.getRole().roleVOToRole();
		user.setRole(role);
		userVO.getAddresses().forEach(addressVO -> {
			  Address address = addressVO.addressVOToAddress();
			  user.addAddress(address);
		});
		user.setBidderCategory(userVO.getBidderCategory().bidderCategoryVOToBidderCategory());
		return this.userDao.save(user).userToUserVO();
	}

	@Override
	public List<UserVO> getAllUsers() {
		return this.userDao.findAll().stream().map(User::userToUserVO).toList();
	}

	@Override
	public void changePassword(Long userId, ChangePasswordVO changePasswordVO) throws Exception {
		User user = this.findUserById(userId);
		if (this.PASSWORDENCODER.matches(changePasswordVO.getOldPassword(), user.getPassword())) {
			user.setPassword(this.PASSWORDENCODER.encode(changePasswordVO.getNewPassword()));
			this.userDao.save(user);
		} else
			throw new Exception("Old password is not correct");
	}

	@Override
	public User findUserById(Long userId) {
		return this.userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

}
