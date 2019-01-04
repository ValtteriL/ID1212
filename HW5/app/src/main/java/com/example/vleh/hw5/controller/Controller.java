package com.example.vleh.hw5.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import com.example.vleh.hw5.net.*;

public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();

    public void connect(String host, int port, OutputHandler outputHandler) throws IOException {
            serverConnection.connect(host, port, outputHandler);
    }

    public void disconnect() throws IOException {
        if (serverConnection.isConnected()) {
            serverConnection.disconnect();
        }
    }

    public void sendGuess(String msg, OutputHandler outputHandler) {
        if (serverConnection.isConnected()) {
            serverConnection.guess(msg);
        } else {
            outputHandler.error();
        }
    }

    public void sendStart(OutputHandler outputHandler) {
        if (serverConnection.isConnected()) {
            serverConnection.startGame();
        } else {
            outputHandler.error();
        }
    }

    public boolean isConnected() {
        return serverConnection.isConnected();
    }

}
