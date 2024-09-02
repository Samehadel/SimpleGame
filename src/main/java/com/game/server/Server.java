package com.game.server;

import static com.game.util.MessageUtils.buildErrorLogMessage;

import com.game.entity.Message;
import com.game.service.MessageProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int MAX_MESSAGES = 10;
    private volatile boolean isRunning = false;

    private final int port;
    private final ExecutorService executorService;
    private final AtomicInteger messageCounter = new AtomicInteger(0);

    private ServerSocketChannel serverSocketChannel;
    private Thread serverThread;

    public Server(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void start() {
        if (isRunning) {
            System.out.println("Server is already running.");
            return;
        }

        isRunning = true;
        serverThread = new Thread(this::runServer);
        serverThread.start();

        System.out.println("Server started on port: " + port);
    }

    private void runServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            while (isRunning) {
                interceptOnSocket();
            }
        } catch (IOException | InterruptedException e) {
            final String errorLogMessage = buildErrorLogMessage(e);
            System.err.println(errorLogMessage);
        } finally {
            cleanup();
        }
    }

    private void interceptOnSocket() throws IOException, InterruptedException {
        final SocketChannel socketChannel = serverSocketChannel.accept();

        if (socketChannel != null) {
            executorService.submit(() -> handleClient(socketChannel.socket()));
        } else {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        if (messageCounter.get() >= MAX_MESSAGES) {
            stopServer();
        }
    }

    private void handleClient(final Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            handleReceivedMessages(in);
        } catch (IOException | ClassNotFoundException e) {
            final String errorLogMessage = buildErrorLogMessage(e);
            System.err.println(errorLogMessage);
        } finally {
            closeSocket(socket);
        }
    }

    private void handleReceivedMessages(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        while (isRunning) {
            final Message receivedMessage = (Message) in.readObject();
            final MessageProcessor messageProcessor = receivedMessage.getReceiver().getMessageProcessor();

            messageProcessor.processMessage(receivedMessage);

            int currentCount = messageCounter.incrementAndGet();

            if (currentCount >= MAX_MESSAGES) {
                break;
            }
        }
    }

    private void closeSocket(final Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            final String errorLogMessage = buildErrorLogMessage(e);
            System.err.println(errorLogMessage);
        }
    }

    public void stopServer() {
        if (!isRunning) {
            System.out.println("Server is not running.");
            return;
        }

        isRunning = false;
        System.out.println("Stopping server after processing " + messageCounter.get() + " messages.");
        cleanup();
    }

    private void cleanup() {
        closeServerResources();
        if (serverThread != null) {
            interruptServerThread();
        }
    }

    private void interruptServerThread() {
        try {
            serverThread.join(5000); // Wait for server thread to finish
        } catch (InterruptedException e) {
            final String errorLogMessage = buildErrorLogMessage(e);
            System.err.println(errorLogMessage);
        }
    }

    private void closeServerResources() {
        closeServerSocketChannel();
        closeExecutorService();

    }

    private void closeServerSocketChannel() {
        try {
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            final String errorLogMessage = buildErrorLogMessage(e);
            System.err.println(errorLogMessage);
        }
    }

    private void closeExecutorService() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("ExecutorService did not terminate in the specified time.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            final String errorLogMessage = buildErrorLogMessage(e);
            System.err.println(errorLogMessage);
        }
    }
}