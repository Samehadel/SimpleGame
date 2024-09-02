package com.game.service;

import static com.game.util.MessageUtils.buildLogMessage;

import com.game.entity.Message;
import com.game.entity.Player;
import com.game.factory.CommunicationServiceFactory;


public class ResponseBackProcessor implements MessageProcessor {
    private Long messagesCount = 1L;

    private CommunicationService communicationService;


    @Override
    public void processMessage(final Message message) {
        this.communicationService = CommunicationServiceFactory.createCommunicationService(message.getSender(), message.getReceiver());

        final String logMessage = buildLogMessage(message);
        System.out.println(logMessage);

        final Message responseMessage = buildResponseMessage(message);

        communicationService.sendMessage(responseMessage);
        messagesCount ++;
    }

    private Message buildResponseMessage(final Message message) {

        final String content = message.getContent() + " - " + this.messagesCount;
        final Player newReceiver = copyPlayer(message.getSender());
        final Player newSender = copyPlayer(message.getReceiver());

        return new Message(content, newSender, newReceiver);
    }

    private Player copyPlayer(final Player player) {
        return new Player.Builder()
            .setName(player.getName())
            .setServerAddress(player.getServerAddress())
            .setServerPort(player.getServerPort())
            .setResponseOption(player.getResponseOption())
            .build();
    }
}
