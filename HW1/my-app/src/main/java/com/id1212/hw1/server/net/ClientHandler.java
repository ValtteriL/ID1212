package com.id1212.hw1.server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;

import com.id1212.hw1.server.controller.*;
import com.id1212.hw1.common.Constants;
import com.id1212.hw1.common.MessageException;
import com.id1212.hw1.common.MsgType;

public class ClientHandler implements Runnable {
    private final HangmanServer server;
    private final Socket clientSocket;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private boolean connected;
    private ArrayList<String> words;
    private Controller contr;

    // constructor
    ClientHandler(HangmanServer server, Socket clientSocket, ArrayList<String> words) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.words = words;
        this.connected = true;
        this.contr = new Controller(words);
    }

    // serve a client
    public void run() {

        try {
            boolean autoFlush = true;
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        while (connected) {
            try {
                int msglength = getMsgLength(fromClient);
                String message = readMessage(fromClient, msglength);
                Message msg = new Message(message);
                switch(msg.msgType) {
                    case START:
                        contr.newRound();
                        sendGD();
                        break;
                    case GUESS:
                        contr.guess(msg.msgBody);
                        sendGD();
                        break;
                    case DISCONNECT:
                        disconnectClient();
                        break;
                    default:
                        throw new MessageException("Received corrupt message: " + msg.receivedString);
                }
            } catch (IOException e) {
                disconnectClient();
                throw new MessageException(e);
            }
        }
    }

    // send game details to client
    void sendGD() {

        StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);
        joiner.add(MsgType.GAMESTATUS.toString());
        joiner.add(contr.getGamestats());

        // calculate message length and prepend length header
        String length = Integer.toString(joiner.length());
        StringJoiner headerjoiner = new StringJoiner(Constants.MSG_DELIMITER);
        headerjoiner.add(length);
        headerjoiner.add(joiner.toString());

        toClient.print(headerjoiner.toString()); // should this be print instead?
        toClient.flush();
    }

    // disconnect client
    private void disconnectClient() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
    }

    // get length of message
    private int getMsgLength(BufferedReader reader) throws IOException{
        int intcharacter;
        StringBuilder buffer = new StringBuilder();
        
        while ((intcharacter = reader.read()) != -1) {
            buffer.append((char) intcharacter);
            if (buffer.toString().indexOf(Constants.MSG_DELIMITER) != -1) {
                // delimiter read
                break;
            }
        }
        int index = buffer.toString().indexOf(Constants.MSG_DELIMITER);

        int length;
        try {
            length = Integer.parseInt(buffer.substring(0, index));
        } catch (Exception e) {
            length = 0;
        }
        
        return length;
    }

    // read message given its length
    private String readMessage(BufferedReader reader, int length) throws IOException {
        char[] buffer = new char[length];
        int readbytes = 0;
        while (readbytes < length) {
            readbytes += reader.read(buffer, readbytes, length-readbytes);
        }

        return String.valueOf(buffer);
    }

    // message definitions
    private static class Message {

        private MsgType msgType;
        private String msgBody;
        private String receivedString;

        private Message(String receivedString) {
            parse(receivedString);
            this.receivedString = receivedString;
        }

        private void parse(String strToParse) {
            try {
                String[] msgTokens = strToParse.split(Constants.MSG_DELIMITER);
                msgType = MsgType.valueOf(msgTokens[Constants.MSG_TYPE_INDEX].toUpperCase());
                if (hasBody(msgTokens)) {
                    msgBody = msgTokens[Constants.MSG_BODY_INDEX];
                }
            } catch (Throwable throwable) {
                throw new MessageException(throwable);
            }
        }
        
        private boolean hasBody(String[] msgTokens) {
            return msgTokens.length > 1;
        }
    }
}