package com.meetEverywhere.common.messages;

/**
 * Created by marekmagik on 2014-09-07.
 */
public class InvitationAcceptedMessage extends Message {

    public static final String TYPE = "InvitationAcceptedMessage";
    private static final long serialVersionUID = 3292424553651518048L;

    public InvitationAcceptedMessage(String text, String authorNickname, String from, String recipient) {
        super(text, authorNickname, from, recipient);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o1) {
        if (o1 != null && o1 instanceof InvitationAcceptedMessage) {
            InvitationAcceptedMessage message = (InvitationAcceptedMessage) o1;
            if (message.getFrom().equals(getFrom())
                    && message.getRecipient().equals(getRecipient())) {
                return true;
            }
        }
        return false;
    }

}
