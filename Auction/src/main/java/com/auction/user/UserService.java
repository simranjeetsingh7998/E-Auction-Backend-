package com.auction.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auction.address.Address;
import com.auction.bidder.category.BidderCategory;
import com.auction.bidder.category.BidderCategoryVO;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.mail.MailSender;
import com.auction.util.GenerateOtp;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;

	private final PasswordEncoder PASSWORDENCODER = new BCryptPasswordEncoder();
	
	@Autowired
	private IUserVerificationDao userVerificationDao;
	
	@Autowired
	private JavaMailSender javaMailSender;

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
	public UserVO findFullUserDetailsById(Long id) {
		User user = this.userDao.findFullUserDetailById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		UserVO userVO = user.userToUserVO();
		userVO.setAddresses(user.getAddresses().stream().map(Address::addressToAddressVO).toList());
		userVO.setRole(user.getRole().roleToRoleVO());
		BidderCategory bidderCategory = user.getBidderCategory();
		BidderCategoryVO bidderCategoryVO = bidderCategory.bidderCategoryToBidderCategoryVO();
		bidderCategoryVO.setBidderType(bidderCategory.getBidderType().bidderTypeToBidderTypeVO());
		userVO.setBidderCategory(bidderCategoryVO);
		return userVO;
	}
	
	@Override
	public void disableUserById(Long id) {
		User user = findUserById(id);
		user.setActive(false);
		this.userDao.save(user);
	}

	@Override
	public User findUserById(Long userId) {
		return this.userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	@Override
	public void sendPhoneOtp(String to) {
		 String otp = GenerateOtp.mobileOtp();
	     this.userVerificationDao.save(new UserVerification(to, otp, false, null));
	}
	
	@Transactional
	@Override
	public void sendEmailOtp(String to) throws Exception {
        String otp = GenerateOtp.emailOtp();
        this.userVerificationDao.save(new UserVerification(to, otp, false, null));
        try {
			new MailSender().sendPlainMail(javaMailSender, to, otp);
		} catch (MessagingException e) {
		   throw new Exception("Error while sending otp");
		}
	}
	
	@Override
	public void verifyEmailOtp(Map<String, String> verificationMap) {
	  verify(verificationMap);
	}
	
	@Override
	public void verifyPhoneOtp(Map<String, String> verificationMap) {
	  verify(verificationMap);
	}
	
	private void verify(Map<String, String> verificationMap) {
		 UserVerification userVerification = this.userVerificationDao.findByPhoneEmailAndOtpAndIsVerifiedFalse(verificationMap.get("to"), verificationMap.get("otp"))
		  .orElseThrow((() -> new ResourceNotFoundException("Verification failed. No entity found")));
		 userVerification.setVerified(true);
		 userVerification.setVerifiedAt(LocalDateTime.now());
		 this.userVerificationDao.save(userVerification);
	}

}
