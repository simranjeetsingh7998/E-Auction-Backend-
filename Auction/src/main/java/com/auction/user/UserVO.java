package com.auction.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.auction.address.AddressVO;
import com.auction.bidder.category.BidderCategoryVO;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserVO {

	private long id;

	@Schema(defaultValue = "First Name", description = "enter first name of user")
	@JsonProperty("first_name")
	@NotBlank(message = "{first_name}")
	private String firstName;
	
	@Schema(defaultValue = "Last Name", description = "enter last name of user")
	@JsonProperty("last_name")
	private String lastName;
	
	@Schema(defaultValue = "Father/Husband First Name", description = "enter Father/Husband First Name of user")
	@JsonProperty("father_husband_first_name")
	private String fatherHusbandFirstName;
	
	@Schema(defaultValue = "Father/Husband Last Name", description = "enter Father/Husband Last name of user")
	@JsonProperty("father_husband_last_name")
	private String fatherHusbandLastName;
	
	@Schema(defaultValue = "Mother First Name", description = "enter mother first name of user")
	@JsonProperty("mother_first_name")
	private String motherFirstName;
	
	@Schema(defaultValue = "Mother Last Name", description = "enter mother last name of user")
	@JsonProperty("mother_last_name")
	private String motherLastName;
	
	@Schema(defaultValue = "User image", description = "upload image of user")
	@JsonProperty("user_image")
	private String userImage;
	
	@Schema(defaultValue = "3754547374", description = "enter mobile number of user")
	@JsonProperty("mobile_number")
	private String mobileNumber;
	
	@Schema(defaultValue = "796355645643", description = "enter aadhar number of user")
	@JsonProperty("aadhar_number")
	private String aadharNumber;
	
	@Schema(defaultValue = "aadhar file ", description = "upload aadhar image of user")
	@JsonProperty("aadhar_file")
	private String aadharFile;
	
	@Schema(defaultValue = "Indian", description = "enter nationality of user")
	private String nationality;

	@Schema(defaultValue = "md@gmail.com", description = "enter email of user")
	private String email;
	
	@Schema(defaultValue = "GFRJR34757334326", description = "enter gst number of user")
	@JsonProperty("gst_number")
	private String gstNumber;

	@Schema(defaultValue = "manojd", description = "enter password of user", minLength = 6)
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must have six digits")
	private String password;
	
	@Schema(defaultValue = "1992-02-20", description = "enter date of birth of user")
	@JsonProperty("birth_day")
	private Date birthDay;
	
	private boolean acknowledge;

	@JsonProperty("active")
	private boolean isActive;

	@Schema( description = "enter role of user")
	@JsonProperty("user_role")
	private RoleVO role;
	
	@Schema(description = "enter bidder type of user")
	@JsonProperty("bidder_category")
	private BidderCategoryVO bidderCategory;
	
	@Schema(description = "enter addresses of user")
	private List<AddressVO> addresses = new ArrayList<>();

	public User userVOToUser() {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(password);
		user.setAadharFile(aadharFile);
		user.setAadharNumber(aadharNumber);
		user.setAcknowledge(acknowledge);
		user.setActive(isActive);
		user.setBirthDay(birthDay);
		user.setFatherHusbandFirstName(fatherHusbandFirstName);
		user.setFatherHusbandLastName(fatherHusbandLastName);
		user.setFirstName(firstName);
		user.setGstNumber(gstNumber);
		user.setLastName(lastName);
		user.setMobileNumber(mobileNumber);
		user.setMotherFirstName(motherFirstName);
		user.setMotherLastName(motherLastName);
		user.setNationality(nationality);
		user.setUserImage(userImage);
		return user;
	}

}
