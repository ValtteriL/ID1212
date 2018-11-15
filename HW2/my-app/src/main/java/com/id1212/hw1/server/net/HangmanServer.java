package com.id1212.hw1.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class HangmanServer {

    private int portNo = 8080;
    private Selector selector;
    private ServerSocketChannel listeningSocketChannel;
    private static final int LINGER_TIME = 5000;
    private final Queue<SelectionKey> sendQueue = new ArrayDeque<>();

    // main
    public static void main(String[] args) {
        HangmanServer server = new HangmanServer();
        server.serve();
    }

    // serve
    private void serve() {
        try {
            // selector + socket to listen for connections
            initSelector();
            initListeningSocketChannel();

            while (true) {

                // check if we have messages to send
                while(!sendQueue.isEmpty()) {
                    sendQueue.poll().interestOps(SelectionKey.OP_WRITE); // 
                }

                selector.select(); // wait for actions

                // there's some action
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            startHandler(key); // accept new client
                        } else if (key.isReadable()) {
                            recvFromClient(key); // receive data from client
                        } else if (key.isWritable()) {
                            sendToClient(key);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Server failed!!!");
        }
    }

    // handle client
    private void startHandler(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept(); // accept new connection
        clientChannel.configureBlocking(false); // don't block
        ClientHandler handler = new ClientHandler(this, clientChannel);
        SelectionKey clientkey = clientChannel.register(selector, SelectionKey.OP_READ, new Client(handler)); // register selector to wait for reads
        handler.setKey(clientkey);
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
    }

    private void initSelector() throws IOException {
        selector = Selector.open();
    }

    private void initListeningSocketChannel() throws IOException {
        listeningSocketChannel = ServerSocketChannel.open();
        listeningSocketChannel.configureBlocking(false);
        listeningSocketChannel.bind(new InetSocketAddress(portNo));
        listeningSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // wait for accept events
    }

    private void recvFromClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
        try {
            client.handler.recv();
        } catch (IOException e) {
            client.handler.disconnectClient();
            key.cancel(); // deregister this client
        }
    }

    // indicated server that a handler has data to send
    public void indicateDataToSend(SelectionKey clientkey) {
        sendQueue.add(clientkey);
        selector.wakeup();
    }

    // send messages to client
    private void sendToClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
        SocketChannel chan = client.handler.getSocketChannel();

        ByteBuffer msg = null;
        synchronized (client.messagesToSend) {
            while ((msg = client.messagesToSend.poll()) != null) {
                // messages left, send
                chan.write(msg);
            }
        }
        key.interestOps(SelectionKey.OP_READ); // after sending all messages we're again interested in reading
    }

    // client - object that is attached to each connection
    private class Client {
        private final ClientHandler handler;
        public Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();

        // constructor
        private Client(ClientHandler handler) {
            this.handler = handler;
            handler.setMessagesToSend(messagesToSend);
        }
    }
}