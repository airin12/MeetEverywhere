package com.meetEverywhere.common;

public class InvitationMessage extends Message {

	private static final long serialVersionUID = 3042786153652017048L;
	public static final String TYPE = "InvitationMessage";

	public InvitationMessage(String text, String authorNickname, String from,
			String recipient) {
		super(text, authorNickname, from, recipient);
	}

	@Override
	public int hashCode() {
		return getText().hashCode() * getFrom().hashCode();
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean equals(Object o1) {
		if (o1 != null && o1 instanceof InvitationMessage) {
			InvitationMessage message = (InvitationMessage) o1;
			if ((message.getFrom().equals(getFrom()) && message.getRecipient()
					.equals(getRecipient()))) {
				return true;
			}
		}
		return false;
	}

}
