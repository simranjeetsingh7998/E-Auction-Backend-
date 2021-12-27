package com.auction.global.exception;

public class JwtExecption extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JwtExecption(String exceptionMessage) {
		super(exceptionMessage);
	}

}
