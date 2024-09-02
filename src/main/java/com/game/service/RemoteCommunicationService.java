package com.game.service;

import com.game.entity.Message;
import com.game.server.Client;

import java.io.IOException;
import java.io.Serializable;


public class RemoteCommunicationService implements CommunicationService, Serializable {
    private static final long serialVersionUID = 1L;
    private Client client;

    @Override
    public void sendMessage(final Message message) {
        initializeClient(message);
        client.sendMessage(message);
    }

    private void initializeClient(final Message message) {
        if (client == null) {
            try {
                client = new Client(message.getReceiver().getServerAddress(), message.getReceiver().getServerPort());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
