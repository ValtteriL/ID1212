package com.example.vleh.hw5.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringJoiner;

import com.example.vleh.hw5.common.*;

public class ServerConnection {
    
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private volatile boolean connected;

    // connect to the server
    public void connect(String host, int port, OutputHandler broadcastHandler) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(broadcastHandler)).start();
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    // disconnect from server
    public void disconnect() throws IOException {
        sendMsg(MsgType.DISCONNECT.toString());
        socket.close();
        socket = null;
        connected = false;
    }

    // start game
    public void startGame() {
        sendMsg(MsgType.START.toString());
    }

    // make a guess
    public void guess(String g) {
        sendMsg(MsgType.GUESS.toString(), g);
    }

    // send message to server
    private void sendMsg(String... parts) {

        StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);
        for (String part : parts) {
            joiner.add(part);
        }

        // calculate message length and prepend length header
        String length = Integer.toString(joiner.length());
        StringJoiner headerjoiner = new StringJoiner(Constants.MSG_DELIMITER);
        headerjoiner.add(length);
        headerjoiner.add(joiner.toString());

        toServer.print(headerjoiner.toString()); // should this be print instead?
        toServer.flush();
    }

    private class Listener implements Runnable {
        
        private final OutputHandler outputHandler;

        private Listener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }

        // get messages and forward them to outputhandler
        public void run() {
            try {
                for (;;) {
                    // get message from server and print it to client
                    int msglength = getMsgLength(fromServer);
                    String message = readMessage(fromServer, msglength);
                    String[] msgParts = extractMsgBody(message);
                    
                    if (msgParts[Constants.MSG_WORDSTATE_INDEX].equals("null")) {
                        // no game ongoing
                        outputHandler.notStarted();
                    } else if(msgParts[Constants.MSG_WORDSTATE_INDEX].indexOf("_") == -1) {
                        // word guessed right
                        outputHandler.win(msgParts[Constants.MSG_WORDSTATE_INDEX], Integer.parseInt(msgParts[Constants.MSG_SCORE_INDEX]));
                    } else {
                        // word not guessed right
                        if (msgParts[Constants.MSG_ATTEMPTS_INDEX].equals("0")) {
                            // all guesses used
                            outputHandler.lose(msgParts[Constants.MSG_WORDSTATE_INDEX], Integer.parseInt(msgParts[Constants.MSG_SCORE_INDEX]));
                        } else {
                            // there are guesses left
                            outputHandler.ongoing(msgParts[Constants.MSG_WORDSTATE_INDEX], Integer.parseInt(msgParts[Constants.MSG_ATTEMPTS_INDEX]));
                        }
                    }
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.error(); // lost connection
                }
            }
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

        // extract the state of the guessed word
        private String[] extractMsgBody(String entireMsg) {
            String[] msgParts = entireMsg.split(Constants.MSG_DELIMITER);
            if (MsgType.valueOf(msgParts[Constants.MSG_TYPE_INDEX].toUpperCase()) != MsgType.GAMESTATUS) {
                throw new MessageException("Received corrupt message: " + entireMsg);
            }
            return msgParts;
        }
    }
}