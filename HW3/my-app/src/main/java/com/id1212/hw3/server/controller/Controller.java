package com.id1212.hw3.server.controller;

import java.util.List;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.id1212.hw3.common.CatalogClient;
import com.id1212.hw3.common.CatalogServer;
import com.id1212.hw3.common.Credentials;
import com.id1212.hw3.server.integration.CatalogDAO;
import com.id1212.hw3.server.model.CatalogFile;
import com.id1212.hw3.server.model.OnlineUser;
import com.id1212.hw3.server.model.Participant;
import com.id1212.hw3.server.model.ParticipantManager;

public class Controller extends UnicastRemoteObject implements CatalogServer {
    private final ParticipantManager participantManager = new ParticipantManager();
    private CatalogDAO database;

    // register the controller at registry
    public Controller() throws RemoteException {
        database = new CatalogDAO();
    }

    // @Override
    public void register(Credentials credentials) throws RemoteException {
        try {
            // check that username doesn't exist
            if (database.findAccByName(credentials.getUsername()) != null) {
                throw new RemoteException("Username taken");
            }

            // create new user
            database.createAcc(new Participant(credentials.getUsername(), credentials.getPassword()));
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    // @Override
    public long login(CatalogClient remoteNode, Credentials credentials) throws RemoteException {
        try {
            // check that credentials match
            Participant participant = database.findAccByName(credentials.getUsername());
            if ((participant == null) || (!participant.getPassword().equals(credentials.getPassword()))) {
                // no such user or wrong password
                throw new RemoteException("Invalid login");
            }

            // add the user to participantManager's list (to keep track of users that are
            // online)
            long participantId = participantManager.createParticipant(remoteNode, participant);
            return participantId;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    // @Override
    public void listfiles(long id) throws RemoteException {
        try {
            // check that user has logged in
            OnlineUser user = participantManager.getOnlineuserById(id);
            if (user == null) {
                throw new RemoteException("Unauthorized request");
            }

            // get all files
            List<CatalogFile> files = database.retrieveFiles();

            // print all files using the client's remote object
            for (CatalogFile file : files) {
                user.send("Filename: " + file.getName() + ", Size: " + file.getSize() + ", Owner: " + file.getOwner()
                        + ", isPublicReadable: " + file.isPublicReadable() + ", isPublicWritable: "
                        + file.isPublicWritable());
            }

        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    // @Override
    public void upload(long id, String filename, long size, boolean isPublicReadable, boolean isPublicWritable)
            throws RemoteException {
        try {
            // check that user has logged in
            Participant user = participantManager.getParticipantById(id);
            if (user == null) {
                throw new RemoteException("Unauthorized request");
            }

            // check if filename exists
            if (database.findFileByName(filename) != null) {
                throw new RemoteException("File already exists");
            }

            // create new file
            database.createCatalogFile(
                    new CatalogFile(filename, size, user.getUsername(), isPublicReadable, isPublicWritable));

        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    // @Override
    public void download(long id, String filename) throws RemoteException {
        try {
            // check that user has logged in
            OnlineUser user = participantManager.getOnlineuserById(id);
            if (user == null) {
                throw new RemoteException("Unauthorized request");
            }

            // check that filename exists
            CatalogFile file = database.findFileByName(filename);
            if (file == null) {
                throw new RemoteException("File doesn't exist");
            }

            // check that the file is marked as public readable or the requester is the
            // owner
            if (!file.isPublicReadable() && !user.getParticipant().getUsername().equals(file.getOwner())) {
                throw new RemoteException("Permission denied");
            }

            // if the file owner is online, notify them using their remote object
            OnlineUser owner = participantManager.getOnlineuserByName(file.getOwner());
            if (owner != null) {
                owner.notify(user.getParticipant().getUsername(), file.getName());
            }
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public void update(long id, String filename, long size) throws RemoteException {
        try {
            // check that user has logged in
            Participant user = participantManager.getParticipantById(id);
            if (user == null) {
                throw new RemoteException("Unauthorized request");
            }

            // check that filename exists
            CatalogFile file = database.findFileByName(filename);
            if (file == null) {
                throw new RemoteException("File doesn't exist");
            }

            // check that the file is marked as public writable or it is owned by the
            // requester
            if (!file.isPublicWritable() && !user.getUsername().equals(file.getOwner())) {
                throw new RemoteException("Permission denied");
            }

            // change the file size
            database.updateFileSize(filename, size);

            // if the file owner is online, notify them using their remote object
            OnlineUser owner = participantManager.getOnlineuserByName(file.getOwner());
            if (owner != null) {
                owner.notify(user.getUsername(), file.getName());
            }
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public void remove(long id, String filename) throws RemoteException {
        try {
            // check that user has logged in
            Participant user = participantManager.getParticipantById(id);
            if (user == null) {
                throw new RemoteException("Unauthorized request");
            }

            // check that filename exists
            CatalogFile file = database.findFileByName(filename);
            if (file == null) {
                throw new RemoteException("File doesn't exist");
            }

            // check that the file is marked as public writable or it is owned by the
            // requester
            if (!file.isPublicWritable() && !user.getUsername().equals(file.getOwner())) {
                throw new RemoteException("Permission denied");
            }

            // delete the file
            database.deleteFileByName(filename);

            // if the file owner is online, notify them using their remote object
            OnlineUser owner = participantManager.getOnlineuserByName(file.getOwner());
            if (owner != null) {
                owner.notify(user.getUsername(), file.getName());
            }
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    // @Override
    public void logout(long id) throws RemoteException {
        // remove user from currently online users
        participantManager.removeParticipant(id);
    }
}