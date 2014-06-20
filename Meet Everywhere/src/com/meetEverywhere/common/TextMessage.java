package com.meetEverywhere.common;

public class TextMessage extends Message{

	private static final long serialVersionUID = 2922796847639314334L;
	
	
	public TextMessage(String text, String authorNickname, String from) {
		super(text, authorNickname, from);
	}
	
	@Override
	public String toString() {
		if (isLocal()) {
			return "Ja: " + getText();
		} else {
			return getAuthorNickname() + ": " + getText();
		}
	}

	@Override
	public int hashCode(){
		return getText().hashCode() * getFrom().hashCode();
	}
}
