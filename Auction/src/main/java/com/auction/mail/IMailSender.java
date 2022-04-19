package com.auction.mail;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.JavaMailSender;

public interface IMailSender {

	void sendPlainMail(JavaMailSender javaMailSender, String to, String message) throws MessagingException;
	void sendMail(EmailObject emailBO);

}
