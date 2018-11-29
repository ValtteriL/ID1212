package com.id1212.hw3.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@NamedQueries({
    @NamedQuery(
	name = "findAccByName",
	query = "SELECT p FROM Participant p WHERE p.username = :username"
    )
})

@Entity(name="Participant")
public class Participant {

    @Id
    @Column(name="username", nullable=false)
    private String username;

    @Column(name="password", nullable=false)
    private String password;

    // Contructor
    public Participant(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // default constructor
    public Participant(){}


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