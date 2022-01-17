package com.auction.global.exception;

public class UserNotVerifiedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotVerifiedException(String message) {
		super(message);
	}

	public UserNotVerifiedException(String message, Throwable cause) {
		super(message, cause);
	}

}
