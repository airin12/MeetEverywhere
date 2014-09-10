package com.meetEverywhere.common.messages;

/**
 * Created by marekmagik on 2014-09-07.
 */
public class FinishAcquiantanceMessage extends Message {

    private static final long serialVersionUID = 2396486643631515388L;
    public static final String TYPE = "FinishAcquiantanceMessage";

    public FinishAcquiantanceMessage(String text, String authorNickname, String from, String recipient) {
        super(text, authorNickname, from, recipient);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o1) {
        if (o1 != null && o1 instanceof FinishAcquiantanceMessage) {
            FinishAcquiantanceMessage message = (FinishAcquiantanceMessage) o1;
            if (message.getFrom().equals(getFrom())
                    && message.getRecipient().equals(getRecipient())) {
                return true;
            }
        }
        return false;
    }
}
