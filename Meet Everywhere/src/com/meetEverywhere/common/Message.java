package com.meetEverywhere.common;

import java.io.Serializable;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = 8755360300459840196L;
	private final String text;
	/* Jeœli null - wiadomoœc napisana przez siebie, jeœli nie - otrzymana. */
	private final String from;
	private final String authorNickname;
	private final String recipient;
	
	public String getRecipient() {
		return recipient;
	}

	public Message(String text, String authorNickname, String from, String recipient) {
		this.text = text;
		this.authorNickname = authorNickname;
		this.from = from;
		this.recipient = recipient;
	}

	public boolean isLocal() {
		if (Configuration.getInstance().getUser().getUserID().equals(getFrom())) {
			return true;
		} else {
			return false;
		}
	}

	public String getFrom() {
		return from;
	}

	public String getAuthorNickname() {
		return authorNickname;
	}

	public String getText() {
		return text;
	}

	public abstract String getType();
}
