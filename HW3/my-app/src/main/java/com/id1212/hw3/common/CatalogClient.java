package com.id1212.hw3.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CatalogClient extends Remote {

    void recvMsg(String msg) throws RemoteException;

    void notify(String user, String file) throws RemoteException;
}