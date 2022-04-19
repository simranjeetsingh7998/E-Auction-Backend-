package com.auction.mail;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mailStatus")
@Data
public class MailStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idMailStatus", unique = true, nullable = false)
	private Integer idMailStatus;
	
	@Column(name = "idUser")
	private String userId;
	
	@Column(name = "recipientList")
	private String recipientList;

	@Column(name = "ccList")
	private String ccList;

	@Column(name = "bccList")
	private String bccList;

	@Column(name = "senderId")
	private String senderId;

	@Column(name = "subject")
	private String subject;

	@Column(name = "mailBody")
	private byte[] mailBody;

	@Column(name = "attachments")
	private String attachments;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sentDate")
	private Date sentDate;

	@Column(name = "imagePathList")
	private String imagePathList;
	
	@Column(name = "emailType")
	private String emailType;
	
	@Column(name = "isSuccess")
	private Boolean isSuccess;
	
	@Column(name = "exception")
	private String exception;
		

}
