package com.id1212.hw3.client.view;

import java.util.Scanner;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.id1212.hw3.common.*;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;

public class NonBlockingInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private final CatalogClient myRemoteObj;
    private CatalogServer server;
    private long myIdAtServer;
    private boolean receivingCmds = false;

    public NonBlockingInterpreter() throws RemoteException {
        myRemoteObj = new ConsoleOutput();
    }

    public void start() {
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        new Thread(this).start();
    }

    public void run() {
        // first look up the server
        try {
            lookupServer("127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // then get commands
        while (receivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                case QUIT:
                    receivingCmds = false;
                    server.logout(myIdAtServer);
                    boolean forceUnexport = false;
                    UnicastRemoteObject.unexportObject(myRemoteObj, forceUnexport);
                    break;
                case LOGIN:
                    myIdAtServer = server.login(myRemoteObj,
                            new Credentials(cmdLine.getParameter(0), cmdLine.getParameter(1)));
                    System.out.println("Login succesful");
                    break;
                case LOGOUT:
                    server.logout(myIdAtServer);
                    System.out.println("Succesfully logged out");
                    break;
                case REGISTER:
                    server.register(new Credentials(cmdLine.getParameter(0), cmdLine.getParameter(1)));
                    System.out.println("Registration succesful");
                    break;
                case LIST:
                    server.listfiles(myIdAtServer);
                    break;
                case REMOVE:
                    server.remove(myIdAtServer, cmdLine.getParameter(0));
                    System.out.println("File removed successfully");
                    break;
                case UPLOAD:
                    server.upload(myIdAtServer, cmdLine.getParameter(0), Long.parseLong(cmdLine.getParameter(1)), (cmdLine.getParameter(2) != null && cmdLine.getParameter(2).equals("public")), (cmdLine.getParameter(3) != null && cmdLine.getParameter(3).equals("public")));
                    System.out.println("File uploaded successfully");
                    break;
                case UPDATE:
                    server.update(myIdAtServer, cmdLine.getParameter(0), Long.parseLong(cmdLine.getParameter(1)));
                    System.out.println("File updated successfully");
                    break;
                case DOWNLOAD:
                    server.download(myIdAtServer, cmdLine.getParameter(0));
                    System.out.println("File downloaded successfully");
                    break;
                case HELP:
                    System.out.println("\nAvailable commands:\nhelp\t\t\t\t\t\tprint this help message\nregister <username> <password>\t\t\tregister new user\nlogin <username> <password>\t\t\tlogin\nlogout\t\t\t\t\t\tlogout\nlist\t\t\t\t\t\tlist files in catalog\nupload <filename> <size> [public] [public]\tupload file to catalog\ndownload <filename>\t\t\t\tdownload file from catalog\nremove <filename>\t\t\t\tremove file from catalog\nupdate <filename> <size>\t\t\tupdate file size\n");
                    break;
                default:
                    System.out.println("No such command. Type 'help' for help");
                }
            } catch (Exception e) {
                outMgr.println("Operation failed: " + e);
            }
        }
    }

    private void lookupServer(String host) throws NotBoundException, MalformedURLException, RemoteException {
        server = (CatalogServer) Naming.lookup("//" + host + "/" + CatalogServer.SERVER_NAME_IN_REGISTRY);
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }

    private class ConsoleOutput extends UnicastRemoteObject implements CatalogClient {

        public ConsoleOutput() throws RemoteException {
        }

        @Override
        public void recvMsg(String msg) {
            outMgr.println((String) msg);
        }

        @Override
        public void notify(String user, String filename) {
            outMgr.println("User " + user + " just accessed your file " + filename);
        }
    }
}