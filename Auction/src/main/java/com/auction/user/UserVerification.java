package com.auction.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_verification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 50414313401591619L;
	
	@Id
	@Column(length = 50)
	private String phoneEmail;
	
	@Column(length = 12)
	private String otp;
	
	private boolean isVerified;
	
	private LocalDateTime verifiedAt;

}
