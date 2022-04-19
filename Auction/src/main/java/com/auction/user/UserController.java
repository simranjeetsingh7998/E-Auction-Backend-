package com.auction.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auction.ControllerHelper;
import com.auction.api.response.ApiResponse;
import com.auction.mail.MailSender;
import com.auction.security.UserDetailImpl;

@Validated
@RestController
public class UserController extends ControllerHelper{

	@PostMapping("/user/bidder")
	public ResponseEntity<ApiResponse> createBidder(@Valid @RequestBody UserVO userVO) {
		userVO.setRole(this.roleService.findByRole(RoleEnum.BIDDER.getRole()));
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), messageResolver.getMessage("user.controller.create"),
				this.userService.createUser(userVO), null), HttpStatus.OK);
	}
	
	@PostMapping("/user/admin")
	public ResponseEntity<ApiResponse> createAdmin(@Valid @RequestBody UserVO userVO) {
		userVO.setRole(this.roleService.findByRole(RoleEnum.ADMIN.getRole()));
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), messageResolver.getMessage("user.controller.create"),
				this.userService.createUser(userVO), null), HttpStatus.OK);
	}
	
	@PostMapping(path = "/upload/document/user/{id}/{documentType}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> addUserDocument(@PathVariable("id") Long userId, @PathVariable String documentType, 
			@RequestPart("document") MultipartFile multipartFile) throws IOException{
		 this.userService.uploadDocument(userId, documentType, multipartFile);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), documentType+" uploaded successfully",
					null, null), HttpStatus.OK);
	}
	

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> userLogin(@RequestBody Login login) {

		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword(), null));
		UserDetailImpl userDetails = this.userServiceDetailImpl.loadUserByUsername(login.getEmail());
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
	public ResponseEntity<ApiResponse> sendEmailOtp(@PathVariable("to") String email) throws Exception{
		 this.userService.sendEmailOtp(email);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Otp has been sent on email Successfully", null, null), HttpStatus.OK);	 
	}
	
	@PostMapping("/user/verify/email/otp")
	public ResponseEntity<ApiResponse> verifyEmailOtp(@RequestBody Map<String, String> verificationMap) throws Exception{
		 this.userService.verifyEmailOtp(verificationMap);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Email verified Successfully", null, null), HttpStatus.OK);	 
	}
	
	@PostMapping("/user/send/mobile/otp/{to}")
	public ResponseEntity<ApiResponse> sendMobileOtp(@PathVariable("to") String mobileNumber) throws Exception{
		 this.userService.sendPhoneOtp(mobileNumber);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Otp has been sent on mobile number Successfully", null, null), HttpStatus.OK);	 
	}
	
	@PostMapping("/user/verify/mobile/otp")
	public ResponseEntity<ApiResponse> verifyMobileOtp(@RequestBody Map<String, String> verificationMap) throws Exception{
		 this.userService.verifyPhoneOtp(verificationMap);
		 return new ResponseEntity<>(
					new ApiResponse(HttpStatus.OK.value(), "Mobile number verified Successfully", null, null), HttpStatus.OK);	 
	}
	
	
}
