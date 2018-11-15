package com.id1212.hw1.client.net;

public interface OutputHandler {

    // called when message is received from the game server
    public void handleMsg(String msg);

    public void handleMsgFormat(String msg);
}