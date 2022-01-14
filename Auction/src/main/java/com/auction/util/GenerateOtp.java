package com.auction.util;

import java.util.SplittableRandom;

import org.thymeleaf.util.StringUtils;

public class GenerateOtp {
	
	private GenerateOtp() {
	}
	
	public static String emailOtp() {
		return StringUtils.randomAlphanumeric(8);
	}
	
	public static String mobileOtp() {
		StringBuilder generatedOTP = new StringBuilder();
		SplittableRandom splittableRandom = new SplittableRandom();
		for (int i = 0; i < 8; i++) {
			int randomNumber = splittableRandom.nextInt(0, 9);
			generatedOTP.append(randomNumber);
		}
		return generatedOTP.toString();
	}

}
