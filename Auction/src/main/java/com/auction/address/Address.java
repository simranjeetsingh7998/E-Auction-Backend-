package com.auction.address;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.auction.user.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="address", indexes = { @Index(columnList = "state"),@Index(columnList = "district"),
		@Index(columnList = "tehsil"),@Index(columnList = "pinCode")})
@Data
@EqualsAndHashCode
public class Address implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3460170638518819545L;

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	private String addressLine1;
	
	private String addressLine2;
	
	private String addressLine3;
	
	@Column(length = 20)
	private String state;
	
	@Column(length = 50)
	private String district;
	
	@Column(length = 50)
	private String tehsil;
	
	@Column(length = 10)
	private Integer pinCode;
	
	private AddressType addressType;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade  = CascadeType.ALL)
	private User user;

	
	public AddressVO addressToAddressVO() {
		AddressVO addressVO = new AddressVO();
		addressVO.setId(id);
		addressVO.setAddressLine1(addressLine1);
		addressVO.setAddressLine2(addressLine2);
		addressVO.setAddressLine3(addressLine3);
		addressVO.setDistrict(district);
		addressVO.setPinCode(pinCode);
		addressVO.setState(state);
		addressVO.setTehsil(tehsil);
		addressVO.setAddressType(addressType);
	return addressVO;
   }
}
