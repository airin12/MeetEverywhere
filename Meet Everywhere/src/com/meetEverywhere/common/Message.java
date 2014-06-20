package com.meetEverywhere.common;

import java.io.Serializable;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;

public abstract class Message implements Serializable{

	private static final long serialVersionUID = 8755360300459840196L;
	private final String text;
	/* Jeœli null - wiadomoœc napisana przez siebie, jeœli nie - otrzymana. */
	private final String from;
	private final String authorNickname;
	

	public Message(String text, String authorNickname, String from) {
		this.text = text;
		this.authorNickname = authorNickname;
		this.from = from;
	}
	
	public boolean isLocal() {
			if(BluetoothDispatcher.getInstance().getOwnData().getUserToken().equals(getFrom())){
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
	
}
