package com.id1212.hw1.client.view;

import java.util.Scanner;

import com.id1212.hw1.client.controller.Controller;
import com.id1212.hw1.client.net.OutputHandler;
import com.id1212.hw1.common.*;

public class Interpreter implements Runnable {

    private Controller contr;
    private Scanner scanner = new Scanner(System.in);
    private boolean gettingCommands = true;
    private final ThreadSafeStdout outMgr = new ThreadSafeStdout();
    
    public void start() {
        // create new controller and start new thread
        contr = new Controller();
        new Thread(this).start();
    }


    public void run() {
        while(gettingCommands) {
            try {
                outMgr.print("Action: ");
                Command command = new Command(scanner.nextLine());
                switch(command.getType()) {
                    case CONNECT:
                        contr.connect(command.getParameter(1), Integer.parseInt(command.getParameter(2)), new ConsoleOutput());
                        break;
                    case QUIT:
                        gettingCommands = false;
                        contr.disconnect();
                        break;
                    case GUESS:
                        contr.sendGuess(command.getParameter(1), new ConsoleOutput());
                        break;
                    case START:
                        contr.sendStart(new ConsoleOutput());
                        break;
                    case HELP:
                        outMgr.println("\nHELP\nhelp\t\t\t\tprint this message\nconnect <host> <ip>\t\tconnect to game server\nstart\t\t\t\tstart new game/round\nguess <character or word>\tmake a guess\nstatus\t\t\t\tshow connection status\nquit\t\t\t\tquit");
                        break;
                    case STATUS:
                        if(contr.isConnected()) {
                            outMgr.println("\nSTATUS:\tConnected to a game server.");
                        } else {
                            outMgr.println("\nSTATUS:\tNot connected.");
                        }
                        break;
                    default:
                        // illegal command, whine about it
                        outMgr.println("Illegal command. Type 'help' for help.");
                }
            } catch (Exception e) {
                outMgr.println("Operation failed");
            }
        }
    }

    private class ConsoleOutput implements OutputHandler {
        // simply print message to screen
        public void handleMsg(String msg) {
            outMgr.println(msg);
        }

        // messages that need some refining
        public void handleMsgFormat(String msg) {

            String[] msgParts = extractMsgBody(msg); // parse the message 
                    
            if (msgParts[Constants.MSG_WORDSTATE_INDEX].equals("null")) {
                // no game ongoing
                handleMsg("No game ongoing. Type \"start\" to start a new game.");
            } else if(msgParts[Constants.MSG_WORDSTATE_INDEX].indexOf("_") == -1) {
                // word guessed right
                handleMsg("You win! The word was \"" + msgParts[Constants.MSG_WORDSTATE_INDEX] + "\". Your score is now " + msgParts[Constants.MSG_SCORE_INDEX] + ". Type \"start\" to start a new round.");
            } else {
                // word not guessed right
                if (msgParts[Constants.MSG_ATTEMPTS_INDEX].equals("0")) {
                    // all guesses used
                    handleMsg("You lost! Your score is now " + msgParts[Constants.MSG_SCORE_INDEX] + ". Type \"start\" to start a new round.");
                } else {
                    // there are guesses left
                    handleMsg("Game ongoing. Wordstate:" + msgParts[Constants.MSG_WORDSTATE_INDEX] + ", Attempts left:" + msgParts[Constants.MSG_ATTEMPTS_INDEX] + ", Score:" + msgParts[Constants.MSG_SCORE_INDEX]);
                }
            }
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