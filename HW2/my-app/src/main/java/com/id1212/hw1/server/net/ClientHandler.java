package com.id1212.hw1.server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;

import com.id1212.hw1.server.controller.*;
import com.id1212.hw1.common.Constants;
import com.id1212.hw1.common.MessageException;
import com.id1212.hw1.common.MsgType;

public class ClientHandler implements Runnable {
    private final SocketChannel socketChannel;
    private Controller contr;
    private ByteBuffer buffer;
    private Message msg;
    private HangmanServer server;
    private SelectionKey key;
    private Queue<ByteBuffer> messagesToSend;


    // constructor
    ClientHandler(HangmanServer server, SocketChannel socketChannel) {
        this.server = server;
        this.socketChannel = socketChannel;
        this.contr = new Controller();
        this.buffer = ByteBuffer.allocate(4096); // should be enough to house all messages
    }

    // serve a client
    public void run() {
        // time consuming tasks
        contr.newRound();
        try {
            sendGD();   
        } catch (IOException e) {
            e.printStackTrace();
            disconnectClient();
        }        
    }

    // receive data to buffer, convert it into a message and schedule this thread to be run
    void recv() throws IOException {
        buffer.clear();
        
        // read whole message
        if (socketChannel.read(buffer) == -1) {
            // throw new ioexception
            throw new IOException("Client closed connection");
        }

        String message = readMessage();
        msg = new Message(message);


        // depending on the message type do action yourself or delegate action to thread pool
        try {
            switch(msg.msgType) {
                case START:
                    ForkJoinPool.commonPool().execute(this); // delegate starting new rounds to thread pool - it includes blocking io
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
        } catch (Exception e) {
            disconnectClient();
            throw new MessageException(e);
        }
    }

    // send game details to client
    void sendGD() throws IOException {

        StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);
        joiner.add(MsgType.GAMESTATUS.toString());
        joiner.add(contr.getGamestats());

        buffer.clear();
        buffer.put(joiner.toString().getBytes());
        buffer.flip();

        // add message to some queue so the server knows to send it
        synchronized (messagesToSend) {
            messagesToSend.add(buffer);
        }
        server.indicateDataToSend(key); // indicate that there's data to be sent
    }

    // disconnect client
    public void disconnectClient() {
        try {
            socketChannel.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // read message
    private String readMessage() {
        buffer.flip(); // flip so we can read it
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes); // read bytes from buffer
        return new String(bytes).replaceAll("\n", "");
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

    /**
     * @return the socketChannel
     */
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    /**
     * @param key the key to set
     */
    public void setKey(SelectionKey key) {
        this.key = key;
    }

    /**
     * @param messagesToSend the messagesToSend to set
     */
    public void setMessagesToSend(Queue<ByteBuffer> messagesToSend) {
        this.messagesToSend = messagesToSend;
    }
}