package com.id1212.hw1.client.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

import com.id1212.hw1.client.net.OutputHandler;
import com.id1212.hw1.client.net.ServerConnection;

public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();

    public void connect(String host, int port, OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            try {
                serverConnection.connect(host, port, outputHandler);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).thenRun(() -> outputHandler.handleMsg("Connected to " + host + ":" + port));
    }

    public void disconnect() throws IOException {
        if (serverConnection.isConnected()) {
            serverConnection.disconnect();
        }
    }

    public void sendGuess(String msg, OutputHandler outputHandler) {
        if (serverConnection.isConnected()) {
            CompletableFuture.runAsync(() -> {
                try {
                    serverConnection.guess(msg);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } else {
            outputHandler.handleMsg("Not connected. Please connect to a server first.");
        }
    }

    public void sendStart(OutputHandler outputHandler) {
        if (serverConnection.isConnected()) {
            CompletableFuture.runAsync(() -> {
                try {
                    serverConnection.startGame();   
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } else {
            outputHandler.handleMsg("Not connected. Please connect to a server first.");
        }
    }

    public boolean isConnected() {
        return serverConnection.isConnected();
    }
}