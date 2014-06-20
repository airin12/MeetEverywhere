package com.meetEverywhere.common;

public class InvitationMessage extends Message {

	private static final long serialVersionUID = 3042786153652017048L;
	
	public InvitationMessage(String text, String authorNickname, String from) {
		super(text, authorNickname, from);
	}

	@Override
	public int hashCode(){
		return getText().hashCode() * getFrom().hashCode();
	}
	
}
