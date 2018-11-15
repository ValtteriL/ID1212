package com.id1212.hw1.server.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import com.id1212.hw1.server.model.*;

public class Controller {

    //private List words;
    private Game game;

    // constructor
    public Controller() {
        this.game = new Game();
    }

    // new round
    public void newRound() {
        chooseWord();
        game.setGameongoing(true);
    }

    // choose word
    private void chooseWord() {

        ArrayList<String> words = readWords(); // read whole file into 

        Random rand = new Random();
        int n = rand.nextInt(words.size());
        String word = words.get(n);
        int attempts = word.length();
        String wordstate = "";
        for(int i = 0; i<attempts; i++) {
            wordstate += "_";
        }

        game.setWord(word);
        game.setAttempts(attempts);
        game.setWordstate(wordstate);
    }

    // make guess
    public void guess(String guess) {

        if (!game.isGameongoing()) {
            // no game ongoing
            return;
        }

        String word = game.getWord();
        int index = word.indexOf(guess); // bug! what if found in multiple places?

        if (index != -1) {
            
            while (index >= 0) {
                // this substring is found in the word!

                // replace characters in wordstate with the one guessed
                char[] wordstateCharArr = game.getWordstate().toCharArray();
                char[] guessCharArr = guess.toCharArray();

                for (int i=0; i<guess.length(); i++) {
                    wordstateCharArr[index+i] = guessCharArr[i];
                }

                // update wordstate
                game.setWordstate(String.valueOf(wordstateCharArr));

                if (game.getWordstate().equals(word)) {
                    // the word has been guessed right!
                    game.setScore(game.getScore()+1);
                    game.setGameongoing(false);
                }

                index = word.indexOf(guess, index + 1);
            }

        } else {
            // wrong guess

            // decrement attempts
            game.setAttempts(game.getAttempts()-1);

            if (game.getAttempts() == 0) {
                // last guess, game over and decrement score
                game.setScore(game.getScore()-1);
                game.setGameongoing(false);
            }
        }
    }

    public String getGamestats() {
        return game.toString();
    }

    // read words to memory
    private ArrayList<String> readWords() {
        File file = new File("words.txt");

        ArrayList<String> words = new ArrayList<String>();
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
        return words;
    }
}