package com.meetEverywhere.common.messages;

import com.meetEverywhere.common.Configuration;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 8755360300459840196L;
    private final String text;
    /* From i recipient zawierają userID. */
    private final String from;
    private final String authorNickname;
    private final String recipient;

    public Message(String text, String authorNickname, String from, String recipient) {
        this.text = text;
        this.authorNickname = authorNickname;
        this.from = from;
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isLocal() {
        return (Configuration.getInstance().getUser().getUserID().equals(getFrom()));
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

    public int hashCode() {
        if (getText() == null) {
            return getFrom().hashCode();
        }
        return getText().hashCode() * getFrom().hashCode();
    }
}
