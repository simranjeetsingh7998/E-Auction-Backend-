package com.auction.global.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8823428752447656023L;

	public ResourceNotFoundException(String exceptionMessage) {
		super(exceptionMessage);

	}

}
