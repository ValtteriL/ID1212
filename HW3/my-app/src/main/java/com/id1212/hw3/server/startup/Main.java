package com.id1212.hw3.server.startup;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.id1212.hw3.server.controller.Controller;

public class Main {

    public static void main(String[] args) {
        try {
            new Main().startRegistry();
            Naming.rebind(Controller.SERVER_NAME_IN_REGISTRY, new Controller());
            System.out.println("Server is running");
        } catch (Exception e) {
            System.out.println("Could not start catalog server");
            e.printStackTrace();
        }
    }

    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (Exception e) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}