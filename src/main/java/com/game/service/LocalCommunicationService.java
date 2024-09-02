package com.game.service;

import com.game.entity.Message;

public class LocalCommunicationService implements CommunicationService {

    @Override
    public void sendMessage(final Message message) {
        final MessageProcessor receiverMessageProcessor = message.getReceiver().getMessageProcessor();
        receiverMessageProcessor.processMessage(message);
    }
}
