package com.id1212.hw3.client.view;

/**
 * Defines all commands that can be performed by a user of the chat application.
 */
public enum Command {

    // log out
    LOGOUT,

    // print help message
    HELP,

    // list files in catalog
    LIST,

    // download file from catalog
    DOWNLOAD,

    // upload file to catalog
    UPLOAD,

    // change file size in catalog
    UPDATE,

    // remove file from catalog
    REMOVE,

    // register new user to catalog
    REGISTER,

    // login to catalog
    LOGIN,

    // quit client application
    QUIT,

    // catch all
    NO_COMMAND
}