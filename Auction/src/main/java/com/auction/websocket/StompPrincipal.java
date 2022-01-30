package com.auction.websocket;
import java.security.Principal;

public class StompPrincipal implements Principal {

	private String user;
	public StompPrincipal(String string) {
		user = string;
	}

	@Override
	public String getName() {
		return user;
	}

}