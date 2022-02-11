package com.auction.bidder.enrollment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "jointHolder")
@Data
public class JointHolder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7731197644250652441L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String firstName;
	
	@Column(length = 50)
	private String lastName;
	
	@Column(length = 50)
	private String fatherHusbandFirstName;
	
	@Column(length = 50)
	private String fatherHusbandLastName;
	
	@Column(length = 15)
	private String mobileNumber;
	
	@Column(length = 15)
	private String gender;
	
	@Column(length = 15)
	private String materialStatus;
	
	private String address1;
	
	private String address2;
	
	private String address3;
	
	@Column(length = 10)
	private String pinCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private BidderAuctionEnrollment bidderAuctionEnrollment;
	
	public JointHolderVO jointHolderToJointHolderVO() {
		 JointHolderVO jointHolderVO = new JointHolderVO();
		 jointHolderVO.setAddress1(address1);
		 jointHolderVO.setAddress2(address2);
		 jointHolderVO.setAddress3(address3);
		 jointHolderVO.setFatherHusbandFirstName(fatherHusbandFirstName);
		 jointHolderVO.setFatherHusbandLastName(fatherHusbandLastName);
		 jointHolderVO.setFirstName(firstName);
		 jointHolderVO.setLastName(lastName);
		 jointHolderVO.setGender(gender);
		 jointHolderVO.setId(id);
		 jointHolderVO.setMaterialStatus(materialStatus);
		 jointHolderVO.setPinCode(pinCode);
		 jointHolderVO.setMobileNumber(mobileNumber);
	  return jointHolderVO;	 
	}
	
}
