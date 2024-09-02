package com.game.entity;

import com.game.enums.ResponseOption;
import com.game.factory.MessageProcessorFactory;
import com.game.service.MessageProcessor;

import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private final String serverAddress;
    private final int serverPort;
    private final MessageProcessor messageProcessor;
    private final ResponseOption responseOption;

    private Player(Builder builder) {
        this.name = builder.name;
        this.serverAddress = builder.serverAddress;
        this.serverPort = builder.serverPort;
        this.messageProcessor = builder.messageProcessor;
        this.responseOption = builder.responseOption;
    }

    public String getName() {
        return name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    public int getServerPort() {
        return serverPort;
    }

    public ResponseOption getResponseOption() {
        return responseOption;
    }

    public static class Builder {
        private String name;
        private String serverAddress;
        private int serverPort;
        private MessageProcessor messageProcessor;
        private ResponseOption responseOption;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public Builder setServerPort(int serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        public Builder setResponseOption(ResponseOption responseOption) {
            this.responseOption = responseOption;
            this.messageProcessor = MessageProcessorFactory.createProcessor(responseOption);
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }
}

