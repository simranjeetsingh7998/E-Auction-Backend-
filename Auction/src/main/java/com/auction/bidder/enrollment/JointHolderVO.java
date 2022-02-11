package com.auction.bidder.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JointHolderVO {
	
	private Long id;

	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonProperty("father_husband_first_name")
	private String fatherHusbandFirstName;
	
	@JsonProperty("father_husband_last_name")
	private String fatherHusbandLastName;
	
	@JsonProperty("mobile_number")
	private String mobileNumber;
	
	@JsonProperty("gender")
	private String gender;
	
	@JsonProperty("material_status")
	private String materialStatus;
	
	private String address1;
	
	private String address2;
	
	private String address3;
	
	@JsonProperty("pin_code")
	private String pinCode;
	
	public JointHolder jointHolderVOToJointHolder() {
		 JointHolder jointHolder = new JointHolder();
		 jointHolder.setAddress1(address1);
		 jointHolder.setAddress2(address2);
		 jointHolder.setAddress3(address3);
		 jointHolder.setFatherHusbandFirstName(fatherHusbandFirstName);
		 jointHolder.setFatherHusbandLastName(fatherHusbandLastName);
		 jointHolder.setFirstName(firstName);
		 jointHolder.setLastName(lastName);
		 jointHolder.setGender(gender);
		 jointHolder.setId(id);
		 jointHolder.setMaterialStatus(materialStatus);
		 jointHolder.setPinCode(pinCode);
		 jointHolder.setMobileNumber(mobileNumber);
	  return jointHolder;	 
	}

}
