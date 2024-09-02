package com.game;

import com.game.entity.Message;
import com.game.entity.Player;
import com.game.enums.ResponseOption;
import com.game.factory.CommunicationServiceFactory;
import com.game.server.Server;
import com.game.service.CommunicationService;

import java.util.Objects;
import java.util.Scanner;


public class SimpleGameApp {

    private static Player initiator;
    private static Player receiver;

    public static void main(String[] args) {
        final int firstPlayerPort = Integer.parseInt(args[0]);
        final int secondPlayerPort = Integer.parseInt(args[1]);

        initializeServer(firstPlayerPort, secondPlayerPort);
        initializePlayers(args);

        if (args.length == 4 || (args.length == 5 && !args[4].equals("receiver"))) {
            processUserInput(initiator, receiver);
        }
    }

    private static void initializePlayers(final String[] args) {
        final String serverAddress = "localhost";
        final int firstPlayerPort = Integer.parseInt(args[0]);
        final int secondPlayerPort = Integer.parseInt(args[1]);
        final String firstPlayerName = args[2];
        final String secondPlayerName = args[3];

        initiator = new Player.Builder()
            .setName(firstPlayerName)
            .setServerAddress(serverAddress)
            .setServerPort(firstPlayerPort)
            .setResponseOption(ResponseOption.JUST_PRINT)
            .build();

        receiver = new Player.Builder()
            .setName(secondPlayerName)
            .setServerAddress(serverAddress)
            .setServerPort(secondPlayerPort)
            .setResponseOption(ResponseOption.RESPOND_BACK)
            .build();
    }

    private static void initializeServer(final int firstPlayerPort, final int secondPlayerPort) {
        if (!Objects.equals(firstPlayerPort, secondPlayerPort)) {
            final Server applicationServer = new Server(firstPlayerPort);
            applicationServer.start();
        }
    }

    private static void processUserInput(final Player initiator, final Player receiver) {
        final CommunicationService communicationService = CommunicationServiceFactory.createCommunicationService(initiator, receiver);
        final Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 10; i++) {
            System.out.println("Enter message: ");

            final String messageContent = scanner.nextLine();
            final Message message = new Message(messageContent, initiator, receiver);
            communicationService.sendMessage(message);
        }
    }
}
