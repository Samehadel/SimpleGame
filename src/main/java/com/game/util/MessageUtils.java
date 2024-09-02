package com.game.util;

import com.game.entity.Message;

public class MessageUtils {
    private MessageUtils() {
    }

    public static String buildLogMessage(final Message message) {
        return String.format("[%s] Received message: [%s] from [%s] on host [%s] and port [%d]\n",
            message.getReceiver().getName(),
            message.getContent(),
            message.getSender().getName(),
            message.getSender().getServerAddress(),
            message.getSender().getServerPort());
    }

    public static String buildErrorLogMessage(final Exception e) {
        return String.format("[%s] Error occurred due to [%s] \n", e.getMessage());
    }
}
