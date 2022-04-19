package com.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;

import com.auction.bidder.category.IBidderCategoryDao;
import com.auction.organization.IOrganizationDao;
import com.auction.security.JwtTokenUtil;
import com.auction.security.UserServiceDetailImpl;
import com.auction.sms.ISMSTemplateDao;
import com.auction.user.IRoleService;
import com.auction.user.IUserDao;
import com.auction.user.IUserService;
import com.auction.user.IUserVerificationDao;
import com.auction.util.FileUpload;

public class ControllerHelper {
	
	@Autowired
	public UserServiceDetailImpl userServiceDetailImpl;

	@Autowired
	public AuthenticationManager authenticationManager;

	@Autowired
	public JwtTokenUtil jwtTokenUtility;

	@Autowired
	public IUserService userService;
	
	@Autowired
	public IRoleService roleService;

	@Autowired
	public JavaMailSender javaMailSender;
	
	@Autowired
	public ApiResponseMessageResolver messageResolver;
	
	@Autowired
	public IUserVerificationDao userVerificationDao;
	
	@Autowired
	public IOrganizationDao organizationDao;
	
	@Autowired
	public IBidderCategoryDao bidderCategoryDao;
	
	@Autowired
	public FileUpload fileUpload;
	
	@Autowired
	public IUserDao userDao;
	
	@Autowired
	public ISMSTemplateDao smsTemplateDao;

}
