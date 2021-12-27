package com.auction.mail;

import javax.mail.MessagingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender implements IMailSender {

	// @Autowired
	// private JavaMailSender javaMailSender;

	@Override
	public void sendPlainMail(JavaMailSender javaMailSender, String to, String message) throws MessagingException {
		SimpleMailMessage mimeMessage = new SimpleMailMessage();
		mimeMessage.setSubject("Test Mail From Java Stack");
		mimeMessage.setText(message);
		mimeMessage.setTo(to);
		javaMailSender.send(mimeMessage);
	}

}
