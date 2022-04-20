package com.auction.mail;

public interface IMailSender {

//	void sendPlainMail(JavaMailSender javaMailSender, String to, String message) throws MessagingException;
	void sendMail(EmailObject emailBO);

}
