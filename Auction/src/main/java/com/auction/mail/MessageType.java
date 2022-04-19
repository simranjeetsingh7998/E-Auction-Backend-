package com.auction.mail;

public enum MessageType {

	TEXT("TEXT"), HTML("HTML");

	private final String value;

	MessageType(String value) {
		this.value = value;
	}

	public static MessageType getInstance(String value) {
		for (MessageType d : MessageType.values()) {
			if (value.equals(d.value))
				return d;
		}
		throw new IllegalArgumentException("No such Message type");

	}

	public String value() {
		return value;
	}
}
