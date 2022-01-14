package com.auction.user;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.ApiResponseMessageResolver;
import com.auction.api.response.ApiResponse;
import com.auction.mail.MailSender;
import com.auction.security.JwtTokenUtil;
import com.auction.security.UserDetailImpl;
import com.auction.security.UserServiceDetailImpl;

@Validated
@RestController
public class UserController {

	@Autowired
	private UserServiceDetailImpl userServiceDetailImpl;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtility;

	@Autowired
	private IUserService userService;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private ApiResponseMessageResolver messageResolver;

	@PostMapping("/user")
	public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserVO userVO) {
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), messageResolver.getMessage("user.controller.create"),
				this.userService.createUser(userVO), null), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> userLogin(@RequestBody UserVO userVO) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userVO.getEmail(), userVO.getPassword(), null));
		UserDetailImpl userDetails = this.userServiceDetailImpl.loadUserByUsername(userVO.getEmail());
		String token = this.jwtTokenUtility.generateToken(userDetails);
		Map<String, Object> response = new HashMap<>(2);
		response.put("access_token", token);
		User user = userDetails.getUser();
		UserVO responseUser = user.userToUserVO();
		responseUser.setRole(user.getRole().roleToRoleVO());
		response.put("user", responseUser);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), "Login Successfully", response, null), HttpStatus.OK);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<ApiResponse> userById(@PathVariable Long id) {
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),messageResolver.getMessage("user.controller.fetch"),
				this.userService.findFullUserDetailsById(id), null), HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{id}/delete")
	public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) {
		this.userService.disableUserById(id);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),messageResolver.getMessage("user.controller.disable"),
				null, null), HttpStatus.OK);
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse> getUsers() {
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), messageResolver.getMessage("user.controller.fetchs"),
				this.userService.getAllUsers(), null), HttpStatus.OK);
	}

	@PutMapping("/user/{id}/change/password")
	public ResponseEntity<ApiResponse> changePassword(@PathVariable("id") Long userId,
			@RequestBody ChangePasswordVO changePasswordVO) throws Exception {
		this.userService.changePassword(userId, changePasswordVO);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), "Password changed Successfully", null, null), HttpStatus.OK);
	}

	@PostMapping("/user/{to}/mail")
	public ResponseEntity<ApiResponse> sendMail(@PathVariable("to") String to) throws MessagingException {
		new MailSender().sendPlainMail(javaMailSender, to, "Testing mail configuration of Java Stack");
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK.value(), "Mail sent Successfully", null, null), HttpStatus.OK);
	}

	@PostMapping("/user/send/email/otp/{to}")
	public ResponseEntity<ApiResponse> sendEmailOtp(@PathVariable("to") String to) throws Exception{
		 this.userService.sendEmailOtp(to);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Verification email sent Successfully", null, null), HttpStatus.OK);	 
	}
	
	@PostMapping("/user/verify/email/otp")
	public ResponseEntity<ApiResponse> verifyEmailOtp(@RequestBody Map<String, String> verificationMap) throws Exception{
		 this.userService.verifyEmailOtp(verificationMap);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Email verified Successfully", null, null), HttpStatus.OK);	 
	}
	
	@PostMapping("/user/send/mobile/otp/{to}")
	public ResponseEntity<ApiResponse> sendMobileOtp(@PathVariable("to") String to) throws Exception{
		 this.userService.sendPhoneOtp(to);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Verification mobile number sent Successfully", null, null), HttpStatus.OK);	 
	}
	
	@PostMapping("/user/verify/mobile/otp")
	public ResponseEntity<ApiResponse> verifyMobileOtp(@RequestBody Map<String, String> verificationMap) throws Exception{
		 this.userService.verifyPhoneOtp(verificationMap);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Mobile number verified Successfully", null, null), HttpStatus.OK);	 
	}
	
	
}
