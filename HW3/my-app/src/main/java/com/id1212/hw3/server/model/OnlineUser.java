package com.id1212.hw3.server.model;


import com.id1212.hw3.common.CatalogClient;

public class OnlineUser {
    private CatalogClient client;
    private Participant participant;

    public OnlineUser(CatalogClient client, Participant participant) {
        this.client = client;
        this.participant = participant;
    }

    /**
     * @return the client
     */
    public CatalogClient getClient() {
        return client;
    }

    /**
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    public void send(String msg) {
        try {
            client.recvMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notify(String user, String filename) {
        try {
            client.notify(user, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}