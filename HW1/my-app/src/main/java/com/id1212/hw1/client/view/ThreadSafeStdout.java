package com.id1212.hw1.client.view;

// class for thread safe output
class ThreadSafeStdout {

    synchronized void print(String msg) {
        System.out.print(msg);
    }

    synchronized void println(String msg) {
        System.out.println(msg);
    }

}