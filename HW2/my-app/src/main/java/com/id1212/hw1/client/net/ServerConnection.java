package com.id1212.hw1.client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.StringJoiner;

import com.id1212.hw1.common.*;

public class ServerConnection {
    
    private SocketChannel channel;
    ByteBuffer buffer = ByteBuffer.allocate(4096);
    private Selector selector;
    private volatile boolean connected;
    private volatile boolean shouldQuit = false; 

    // connect to the server
    public void connect(String host, int port, OutputHandler broadcastHandler) throws IOException {
        channel = SocketChannel.open(new InetSocketAddress(host, port));
        channel.configureBlocking(false);
        selector = Selector.open();

        channel.register(selector, SelectionKey.OP_READ); // register socketchannel for reading
        
        connected = true;
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
        channel.close();
        connected = false;

        // indicate the listening thread to stop
        shouldQuit = true;
        selector.wakeup();
    }

    // start game
    public void startGame() throws IOException {
        sendMsg(MsgType.START.toString());
    }

    // make a guess
    public void guess(String g) throws IOException {
        sendMsg(MsgType.GUESS.toString(), g);
    }

    // send message to server
    private void sendMsg(String... parts) throws IOException {

        StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);
        for (String part : parts) {
            joiner.add(part);
        }

        buffer.clear();
        buffer.put(joiner.toString().getBytes());

        buffer.flip();
        channel.write(buffer);
    }

    // read message from channel to buffer
    private void recv() throws IOException {
        buffer.clear();
        
        // read whole message
        if (channel.read(buffer) == -1) {
            // throw new ioexception
            throw new IOException("Client closed connection");
        }
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

                    if (shouldQuit) {
                        break;
                    }

                    selector.select(); // wait for data

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()) {

                        iterator.next();
                        iterator.remove();

                        recv(); // read data from channel to buffer
                        String message = readMessage(); // read message from buffer
                        outputHandler.handleMsgFormat(message);
                    }
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.handleMsg("Lost connection.");
                }
            }
        }


        // read message
        private String readMessage() {
            buffer.flip(); // flip so we can read it
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes); // read bytes from buffer
            return new String(bytes).replaceAll("\n", "");
        }
    }
}