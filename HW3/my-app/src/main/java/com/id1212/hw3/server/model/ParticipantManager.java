package com.id1212.hw3.server.model;

import com.id1212.hw3.common.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ParticipantManager {
    private final Random idGenerator = new Random();
    private final Map<Long, OnlineUser> onlineUsers = Collections.synchronizedMap(new HashMap<Long, OnlineUser>());

    // retrieve onlineuser by id
    public OnlineUser getOnlineuserById(long id) {
        return onlineUsers.get(id);
    }

    // retrieve participant by id
    public Participant getParticipantById(long id) {
        return onlineUsers.get(id).getParticipant();
    }

    // retrieve onlineuser by name
    public OnlineUser getOnlineuserByName(String name) {
        Iterator<Map.Entry<Long, OnlineUser>> it = onlineUsers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, OnlineUser> pair = it.next();
            Participant part = pair.getValue().getParticipant();
            if (part.getUsername().equals(name)) {
                return pair.getValue();
            }
        }
        return null;
    }

    // retrieve participant by name
    public Participant getParticipantByName(String name) {
        Iterator<Map.Entry<Long, OnlineUser>> it = onlineUsers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, OnlineUser> pair = it.next();
            Participant part = pair.getValue().getParticipant();
            if (part.getUsername().equals(name)) {
                return part;
            }
        }
        return null;
    }

    // create new participant (login)
    public long createParticipant(CatalogClient remoteNode, Participant participant) {
        long participantId = idGenerator.nextLong();
        onlineUsers.put(participantId, new OnlineUser(remoteNode, participant));
        return participantId;
    }

    // remove participant (logout)
    public void removeParticipant(long id) {
        onlineUsers.remove(id);
    }
}