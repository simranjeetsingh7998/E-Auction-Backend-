package com.auction.user;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.auction.address.Address;
import com.auction.bidder.category.BidderCategory;
import com.auction.bidder.category.BidderCategoryVO;
import com.auction.bidder.category.IBidderCategoryDao;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.global.exception.UserNotVerifiedException;
import com.auction.organization.IOrganizationDao;
import com.auction.organization.Organization;
import com.auction.sms.ISMSTemplateDao;
import com.auction.sms.SMSUtility;
import com.auction.util.FileUpload;
import com.auction.util.GenerateOtp;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private IUserVerificationDao userVerificationDao;
	
	@Autowired
	private IOrganizationDao organizationDao;
	
	@Autowired
	private IBidderCategoryDao bidderCategoryDao;
	
	@Autowired
	private FileUpload fileUpload;
	
	@Autowired
	public ISMSTemplateDao smsTemplateDao;

	@Transactional
	@Override
	public UserVO createUser(UserVO userVO) {
		if(userVO.getRole().getRole().equals(RoleEnum.BIDDER.getRole())) {
			isVerified(userVO.getEmail(), userVO.getMobileNumber());
		}
		User user = userVO.userVOToUser();
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role = userVO.getRole().roleVOToRole();
		user.setRole(role);
		userVO.getAddresses().forEach(addressVO -> {
			  Address address = addressVO.addressVOToAddress();
			  user.addAddress(address);
		});
		//user.setBidderCategory(userVO.getBidderCategory().bidderCategoryVOToBidderCategory());
		user.setBidderCategory(this.bidderCategoryDao.findAllByIsActiveTrueAndBidderTypeId(userVO.getBidderType().getId()).get(0));
		Organization organization = this.organizationDao.findById(1).orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
//		Organization organization = userVO.getOrganization().organizationVOToOrganization();
//		if(organization.getId()==0 || Objects.isNull(organization.getId())) {
//			organization = this.organizationDao.save(organization);
//		}
		user.setOrganization(organization);
		return this.userDao.save(user).userToUserVO();
	}
	
	private void isVerified(String email, String phone) {
	    Optional<UserVerification> userVerification = this.userVerificationDao.findByPhoneEmailAndIsVerifiedTrue(phone);
	    if(!userVerification.isPresent())
	    	 throw new UserNotVerifiedException("Phone number is not verified");
	     userVerification = this.userVerificationDao.findByPhoneEmailAndIsVerifiedTrue(email);
	    if(!userVerification.isPresent())
	    	 throw new UserNotVerifiedException("Email is not verified");
	}
	
	@Transactional
	@Override
	public void uploadDocument(Long userId, String documentType, MultipartFile multipartFile) throws IOException {
		User user = this.findUserById(userId);
		StringBuilder directory = new StringBuilder();
		directory.append( FileUpload.USERDIRECTORY);
		directory.append(File.separator);
		directory.append(user.getId());
		StringBuilder fileName = new StringBuilder(documentType);
		String fileOriginalName = multipartFile.getOriginalFilename();
		fileName.append(fileOriginalName.substring(fileOriginalName.lastIndexOf(".")));
		if(UserDocumenType.AADHARCARD.name().equalsIgnoreCase(documentType)) {
			 this.fileUpload.uploadMultipartDocument(directory.toString(), fileName.toString(), multipartFile);
			 user.setAadharFile(directory.append(File.separator).toString()+fileName.toString());
		} else if(UserDocumenType.PANCARD.name().equalsIgnoreCase(documentType)) {
			 this.fileUpload.uploadMultipartDocument(directory.toString(), fileName.toString(), multipartFile);
			 user.setPanCardFile(directory.append(File.separator).toString()+fileName.toString());
		} else if(UserDocumenType.PROFILE.name().equalsIgnoreCase(documentType)) {
			 this.fileUpload.uploadMultipartDocument(directory.toString(), fileName.toString(), multipartFile);
			 user.setUserImage(directory.append(File.separator).toString()+fileName.toString());
		}
		this.userDao.save(user);
	}

	@Override
	public List<UserVO> getAllUsers() {
		return this.userDao.findAll().stream().map(User::userToUserVO).toList();
	}

	@Override
	public void changePassword(Long userId, ChangePasswordVO changePasswordVO) throws Exception {
		User user = this.findUserById(userId);
		if (this.passwordEncoder.matches(changePasswordVO.getOldPassword(), user.getPassword())) {
			user.setPassword(this.passwordEncoder.encode(changePasswordVO.getNewPassword()));
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
		//userVO.setBidderCategory(bidderCategoryVO);
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
		 UserVerification user= this.userVerificationDao.save(new UserVerification(to, otp, false, null));
		 if(null!=user) {
			 StringBuilder sms = new StringBuilder("Dear candidate, The otp for your transaction is ");
			 sms.append(user.getOtp());
			 sms.append(". This otp is valid for 30 min - Regards Profices.");
			 String smsStatus = SMSUtility.sendSMS(sms.toString(),user.getPhoneEmail(),smsTemplateDao.findByTemplateName("proficesotptran").get().getTemplateId());
			 if (!"200".equalsIgnoreCase(smsStatus)) {
				 throw new ResourceNotFoundException("Error in sending SMS");
			 }
		 }
	}
	
	@Transactional
	@Override
	public void sendEmailOtp(String to) throws Exception {
        String otp = GenerateOtp.emailOtp();
        this.userVerificationDao.save(new UserVerification(to, otp, false, null));
//        try {
//			new MailSender().sendPlainMail(javaMailSender, to, otp);
//		} catch (MessagingException e) {
//		   throw new Exception("Error while sending otp");
//		}
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
