package com.auction.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.auction.address.AddressVO;
import com.auction.bidder.BidderTypeVO;
import com.auction.organization.OrganizationVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserVO {

	private long id;

	@Schema(defaultValue = "First Name", description = "enter first name of user")
	@JsonProperty("first_name")
	@NotBlank(message = "{first_name}")
	private String firstName;
	
	@Schema(defaultValue = "Last Name", description = "enter last name of user")
	@JsonProperty("last_name")
	@NotBlank(message = "{last_name}")
	private String lastName;
	
	@Schema(defaultValue = "Father/Husband First Name", description = "enter Father/Husband First Name of user")
	@JsonProperty("father_husband_first_name")
	@NotBlank(message = "{father_husband_first_name}")
	private String fatherHusbandFirstName;
	
	@Schema(defaultValue = "Father/Husband Last Name", description = "enter Father/Husband Last name of user")
	@JsonProperty("father_husband_last_name")
	@NotBlank(message = "{father_husband_last_name}")
	private String fatherHusbandLastName;
	
	@Schema(defaultValue = "Mother First Name", description = "enter mother first name of user")
	@JsonProperty("mother_first_name")
	@NotBlank(message = "{mother_first_name}")
	private String motherFirstName;
	
	@Schema(defaultValue = "Mother Last Name", description = "enter mother last name of user")
	@JsonProperty("mother_last_name")
	@NotBlank(message = "{mother_last_name}")
	private String motherLastName;
	
	@Schema(defaultValue = "User image", description = "upload image of user")
	@JsonProperty("user_image")
	private String userImage;
	
	@Schema(defaultValue = "3754547374", description = "enter mobile number of user")
	@JsonProperty("mobile_number")
	@NotBlank(message = "{mobile_number}")
	@Size(min = 10, max = 10, message = "{mobile_number_size}")
	private String mobileNumber;
	
	@Schema(defaultValue = "796355645643", description = "enter aadhar number of user")
	@JsonProperty("aadhar_number")
	@NotBlank(message = "{aadhar_number}")
	@Size(min = 12, max = 12, message = "{aadhar_number_size}")
	private String aadharNumber;
	
	@Schema(defaultValue = "aadhar file ", description = "upload aadhar image of user")
	@JsonProperty("aadhar_file")
	private String aadharFile;
	
	@Schema(defaultValue = "Indian", description = "enter nationality of user")
	private String nationality;

	@Schema(defaultValue = "md@gmail.com", description = "enter email of user")
	@NotBlank(message = "{email}")
	@Email(message = "{email_pattern}")
	private String email;

	@Schema(defaultValue = "manojd", description = "enter password of user", minLength = 6)
	@NotBlank(message = "{password}")
	@Size(min = 6, message = "{password_size}")
	private String password;
	
	@JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
	@Schema(defaultValue = "1992-02-20", description = "enter date of birth of user")
	@JsonProperty(value ="birth_day",required = false)
 //   @NotBlank(message = "{birth_day}")
//	@Past(message = "{past_date}")
	private Date birthDay;
	
	@JsonProperty("marital_status")
    @NotBlank(message = "{martial_status}")
	private String maritalStatus;
	
	@NotBlank(message = "{gender}")
	private String gender;
	@JsonProperty("comapany_name")
	private String companyFirmName;
	@NotBlank(message = "{pan_card_number}")
	@Size(min = 10, max = 10, message = "{pan_card_number_size}")
	@JsonProperty("pan_number")
	private String panCardNumber;
	@JsonProperty("pan_file")
	private String panCardFile;
	@JsonProperty("legal_status")
	private String legalStatus;
	private String designation;
	
	@Schema(defaultValue = "GFRJR34757334326", description = "enter gst number of user")
	@JsonProperty("gst_number")
	private String gstNumber;
	
	private boolean acknowledge;

	@JsonProperty("active")
	private boolean isActive;

	@Schema( description = "enter role of user")
	@JsonProperty("user_role")
	private RoleVO role;
	
//	@Schema(description = "enter bidder type of user")
//	@JsonProperty("bidder_category")
//	private BidderCategoryVO bidderCategory;
	
	@JsonProperty("bidder_type")
	private BidderTypeVO bidderType;
	
	private OrganizationVO organization;
	
//	@Valid
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
		user.setLegalStatus(legalStatus);
		user.setDesignation(designation);
		user.setGender(gender);
		user.setCompanyFirmName(companyFirmName);
		user.setPanCardNumber(panCardNumber);
		user.setPanCardFile(panCardFile);
		
		return user;
	}

}
