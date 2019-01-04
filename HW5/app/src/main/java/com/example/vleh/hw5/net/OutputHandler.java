package com.example.vleh.hw5.net;

public interface OutputHandler {

    // called when message is received from the game server
    public void notStarted();
    public void win(String word, int score);
    public void ongoing(String word, int attempts);
    public void lose(String word, int score);
    public void error();
}