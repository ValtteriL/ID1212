package com.id1212.hw1.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class HangmanServer {

    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final ArrayList<String> words = new ArrayList<String>();
    private int portNo = 8080;


    // main
    public static void main(String[] args) {
        HangmanServer server = new HangmanServer();
        server.readWords();
        server.serve();
    }

    // serve
    private void serve() {

        try {
            ServerSocket ssock = new ServerSocket(portNo);
            while(true) {
                System.out.println("Ready to accept connections!");
                Socket clientSocket = ssock.accept(); // accept new client
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("IOException occurred in server");
        }
    }

    // handle client
    private void startHandler(Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME); // try to send data before closing socket
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR); // time to keep trying

        ClientHandler handler = new ClientHandler(this, clientSocket, words);

        // create and start a thread to handle the client
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }

    // read words to memory
    private void readWords() {
        File file = new File("words.txt");
        BufferedReader breader;
        try {
            breader = new BufferedReader(new FileReader(file));
            String line = breader.readLine();
            while (line != null) {
                words.add(line);
                line = breader.readLine();
            }
            breader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}