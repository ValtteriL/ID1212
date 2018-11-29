package com.id1212.hw3.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CatalogServer extends Remote {
    
    // URI to the catalog server in the RMI  regsitry
    public static final String SERVER_NAME_IN_REGISTRY = "CATALOG_SERVER";

    // register at the catalog
    void register(Credentials credentials) throws RemoteException;

    // login to the server
    long login(CatalogClient remoteNode, Credentials credentials) throws RemoteException;

    // list files
    void listfiles(long id) throws RemoteException;

    // upload file to catalog
    void upload(long id, String filename, long size, boolean isPublicReadable, boolean isPublicWritable) throws RemoteException;

    // download file from catalog
    void download(long id, String filename) throws RemoteException;

    // update file in catalog
    void update(long id, String filename, long size) throws RemoteException;

    // remove file from catalog
    void remove(long id, String filename) throws RemoteException;

    // logout from the server
    void logout(long id) throws RemoteException;
}