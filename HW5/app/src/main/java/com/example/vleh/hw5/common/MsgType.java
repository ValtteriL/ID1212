package com.example.vleh.hw5.common;

public enum MsgType {
    // client wants to start a new round in the game
    START,

    // client makes a guess, either a character or a word
    GUESS,

    // client is about to disconnect from the game server
    DISCONNECT,

    // server informs the client about the status of the game
    GAMESTATUS
}