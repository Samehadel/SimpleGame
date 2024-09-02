package com.game.factory;

import com.game.entity.Player;
import com.game.service.CommunicationService;
import com.game.service.LocalCommunicationService;
import com.game.service.RemoteCommunicationService;

import java.util.Objects;

public class CommunicationServiceFactory {
    private static final CommunicationService LOCAL_COMMUNICATION_SERVICE = new LocalCommunicationService();
    private static final CommunicationService REMOTE_COMMUNICATION_SERVICE = new RemoteCommunicationService();

    private CommunicationServiceFactory() {
    }

    public static CommunicationService createCommunicationService(final Player sender, final Player receiver) {
        final boolean isLocals = Objects.equals(sender.getServerAddress(), receiver.getServerAddress())
            && sender.getServerPort() == receiver.getServerPort();

        if (isLocals) {
            return LOCAL_COMMUNICATION_SERVICE;
        } else {
            return REMOTE_COMMUNICATION_SERVICE;
        }
    }
}
