package com.auction.mail;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "emailsetting")
@Data
@ToString
public class EmailSetting implements Serializable{
	
	private static final long serialVersionUID = 7375691675485210256L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idEmailSetting", unique = true, nullable = false)
	private Integer emailSettingId;

	@Column(name = "idOrganization")
	private Integer idOrganization;
	
	@Column(name = "emailType")
	private String emailType;

	@Column(name = "emailSubject")
	private String emailSubject;
	
	@Column(name = "emailMessage")
	private String emailMessage;
	
	@Column(name = "ccMailAddress")
	private String ccMailAddress;
	
	@Column(name = "fromAddress")
	private String fromAddress;
	
	@Column(name = "isActive")
	private String isActive;
	
	@Transient
	private String bgColor;	

}
