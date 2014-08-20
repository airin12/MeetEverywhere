package com.meetEverywhere.common;

public class TextMessage extends Message {

	private static final long serialVersionUID = 2922796847639314334L;
	public static final String TYPE = "TextMessage";

	public TextMessage(String text, String authorNickname, String from,
			String recipient) {
		super(text, authorNickname, from, recipient);
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
	public int hashCode() {
		return getText().hashCode() * getFrom().hashCode();
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean equals(Object o1) {
		if (o1 != null && o1 instanceof TextMessage) {
			TextMessage message = (TextMessage) o1;
			if (message.getFrom().equals(getFrom())
					&& message.getRecipient().equals(getRecipient())
					&& message.getText().equals(getText())) {
				return true;
			}
		}
		return false;
	}
}
