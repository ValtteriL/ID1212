package com.id1212.hw1.server.model;

import java.util.StringJoiner;
import com.id1212.hw1.common.*;

public class Game {

    private int score = 0;
    private int attempts;
    private String word;
    private String wordstate;
    private boolean gameongoing = false;


    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }
    /**
     * @return the attempts
     */
    public int getAttempts() {
        return attempts;
    }
    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }
    /**
     * @return the wordstate
     */
    public String getWordstate() {
        return wordstate;
    }
    /**
     * @return the gameongoing
     */
    public boolean isGameongoing() {
        return gameongoing;
    }
    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * @param attempts the attempts to set
     */
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    /**
     * @param gameongoing the gameongoing to set
     */
    public void setGameongoing(boolean gameongoing) {
        this.gameongoing = gameongoing;
    }
    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }
    /**
     * @param wordstate the wordstate to set
     */
    public void setWordstate(String wordstate) {
        this.wordstate = wordstate;
    }


    @Override
    /**
     * Return the state of the game in a format that it can be appended to a message
     */
    public String toString() {
        StringJoiner joiner = new StringJoiner(Constants.MSG_DELIMITER);
        joiner.add(wordstate);
        joiner.add(Integer.toString(attempts));
        joiner.add(Integer.toString(score));
        return joiner.toString();
    }
}