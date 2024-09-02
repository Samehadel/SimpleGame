package com.game.server;

import static com.game.util.MessageUtils.buildErrorLogMessage;

import com.game.entity.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Serializable {
    private final ObjectOutputStream out;
    private final Socket socket;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            final String errorMessage = buildErrorLogMessage(e);
            System.err.println(errorMessage);
        }
    }
}
