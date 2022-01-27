package com.auction.global.exception;

public class DataMisMatchException  extends RuntimeException {

	private static final long serialVersionUID = 8823428752447656023L;

	public DataMisMatchException(String exceptionMessage) {
		super(exceptionMessage);

	}
}
