package com.auction.global.exception;

public class ResourceAlreadyExist extends RuntimeException {

	private static final long serialVersionUID = 8823428752447656023L;

	public ResourceAlreadyExist(String exceptionMessage) {
		super(exceptionMessage);

	}

}