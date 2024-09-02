package com.game.service;

import static com.game.util.MessageUtils.buildLogMessage;

import com.game.entity.Message;

public class NoResponseProcessor implements MessageProcessor {

    @Override
    public void processMessage(final Message message) {
        final String logMessage = buildLogMessage(message);

        System.out.println(logMessage);
    }

}
