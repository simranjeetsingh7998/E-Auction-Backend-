package com.auction.mail;

import java.util.Date;
import java.util.List;

import javax.mail.Transport;

import lombok.Data;

@Data
public class EmailObject {
	
	private String userId;

	private String[] recipientList;

	private String sender;

	private String subject;
	
	private String mailBody;

	private String[] bccList;

	private String[] ccList;
	
	private List<String> attachments;

	private boolean ccRequired;

	private Date sentDate;

	private MessageType messageType;
	
	private List<String> imagePathList;
	
	private Transport transport;
	
	private MailStatus mailStatus;
	
	private String emailType;
	
	private Long OTP;
	

}
