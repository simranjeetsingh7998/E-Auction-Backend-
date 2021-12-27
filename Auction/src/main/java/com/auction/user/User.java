package com.auction.user;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auction.address.Address;
import com.auction.bidder.category.BidderCategory;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "userVOs")
@Data
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 91280613871261500L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String firstName;
	
	@Column(length = 50)
	private String lastName;
	
	@Column(length = 50)
	private String fatherHusbandFirstName;
	
	@Column(length = 50)
	private String fatherHusbandLastName;
	
	@Column(length = 50)
	private String motherFirstName;
	
	@Column(length = 50)
	private String motherLastName;
	
	@Column(length = 100)
	private String userImage;
	
	@Column(length = 15)
	private String mobileNumber;
	
	@Column(length = 16)
	private String aadharNumber;
	
	@Column(length = 100)
	private String aadharFile;
	
	@Column(length = 50)
	private String nationality;

	@Column(length = 100)
	private String email;
	
	@Column(length = 50)
	private String gstNumber;

	private String password;
	
	private Date birthDay;
	
	private boolean acknowledge;

	private boolean isActive;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Role role;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private BidderCategory bidderCategory;
	
	@OneToMany(mappedBy = "user", cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private Set<Address> addresses = new HashSet<>();

	public List<GrantedAuthority> getUserGrantedAuthority() {
		return Collections.singletonList(new SimpleGrantedAuthority(this.getRole().getRole()));
	}
	
	public void addAddress(Address address) {
		this.addresses.add(address);
		address.setUser(this);
	}

	public UserVO userToUserVO() {
		UserVO userVO = new UserVO();
		userVO.setId(id);
		userVO.setEmail(email);
		userVO.setPassword(password);
		userVO.setAadharFile(aadharFile);
		userVO.setAadharNumber(aadharNumber);
		userVO.setAcknowledge(acknowledge);
		userVO.setActive(isActive);
		userVO.setBirthDay(birthDay);
		userVO.setFatherHusbandFirstName(fatherHusbandFirstName);
		userVO.setFatherHusbandLastName(fatherHusbandLastName);
		userVO.setFirstName(firstName);
		userVO.setGstNumber(gstNumber);
		userVO.setLastName(lastName);
		userVO.setMobileNumber(mobileNumber);
		userVO.setMotherFirstName(motherFirstName);
		userVO.setMotherLastName(motherLastName);
		userVO.setNationality(nationality);
		userVO.setUserImage(userImage);;
		return userVO;
	}

}
