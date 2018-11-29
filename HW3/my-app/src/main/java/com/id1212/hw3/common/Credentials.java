package com.id1212.hw3.common;

import java.io.Serializable;

public class Credentials implements Serializable {
    private final String username;
    private final String password;

    // Constructor
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}