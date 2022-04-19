package com.auction.mail;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MailConfig implements WebMvcConfigurer{
	
	
	@Bean
	public JavaMailSender mailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("email-smtp.ap-south-1.amazonaws.com");
	    mailSender.setUsername("AKIA6DYX4US5FL3F4VVY");
	    mailSender.setPassword("BIDfzUGILi5Iog3AYSaocF/RGzi4R0ZZQE/hWesqs/+J");
	    mailSender.setPort(465);
	    mailSender.setProtocol("smtps");

	    Properties properties = new Properties();
	    properties.setProperty("mail.smtps.auth", "true");
	    properties.setProperty("mail.smtp.ssl.enable", "true");
	    properties.setProperty("mail.debug", "true");
	    mailSender.setJavaMailProperties(properties);
	    return mailSender;
	}
	
}