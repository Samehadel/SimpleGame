package com.game.entity;

import java.io.Serializable;

public class Message implements Serializable {
    private final String content;
    private final Player sender;
    private final Player receiver;

    public Message(String content, Player sender, Player receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return "Message{" +
            "content='" + content + '\'' +
            ", sender=" + sender.getName() +
            ", receiver=" + receiver.getName() +
            '}';
    }
}
