/*
 * ClientCommand.java
 *
 * Created on 7 December 2002, 23:40
 */

package server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author  Ken Barber
 */
public class ClientCommand {
    
    /* A pointer to the passed UserData object. */
    private UserData userData;
    
    /* These will be used for network commands. See the constructor. */
    private Charset charset;
    private CharsetDecoder decoder;
    private CharsetEncoder encoder;
    
    /** 
     * Creates a new instance of ClientCommand.
     */
    public ClientCommand(UserData ud) {
        /* Keep a local pointer to the user data object */
        userData = ud;

        /* Setup charset,decoder and encoder for use by networking commands*/
        charset = Charset.forName("ISO-8859-1");
        decoder = charset.newDecoder();
        encoder = charset.newEncoder();
    }

    /** 
     * Responsible for the proper signup of new clients. 
     */
    public void clientSignup(String un, SocketChannel sc) {
        /* See if the user registration is valid */
        if(userData.insertData(un, sc)) {
            /* Send a message to all users about the new user */
            messageAll("signup", un);

            /* Notify on the terminal that new user has signed up */
            System.out.println(new Date().toString() + ": New user signed in: \"" + un + "\"");
        } else {
            /* Let the user know that there was an error with signup. */
            returnError("Already registered, or username taken", sc);

            /* Notify on the terminal about the new user */
            System.out.println(new Date().toString() + ": Attempt to sign in with duplicate username: " + un);
        }
    }

    /**
     * This method is responsible for the proper terminate of a user
     * when a quit command is submitted.
     */
    public void clientQuit(String pa, SocketChannel sc) {
        /* Get the username using the given SocketChannel */
        String userName = userData.getName(sc);
            
        /* Let everyone know that the user has quit */
        messageAll("quit", userName + "," + pa);
            
        /* Remove the users entry from the UserData object */
        userData.deleteEntry(sc);
            
        /* Notify on the terminal that the user has quit */
        System.out.println(new Date().toString() + ": User quit: \"" + userName + "\" because \"" + pa + "\"");
    }
    
    /**
     * Deal with a request to see the list of users.
     */
    public void clientList(String se, SocketChannel sc) {
        /* Obtain an array which contains a list of the currently
         * logged in users */
        Object[] users = userData.listNames();

        /* Parse the array of users, and create a single comma delimited
         * string */
        String list = new String();
        for(int loop = 0; loop <= (users.length -1); loop++) {
            list = list + users[loop] + ",";
        }           
        
        /* Return the list of users to the user who requested it */
        message("list", list, sc);
    }
    
    /**
     * Deal with requests for a user message to be sent.
     */
    public void clientSend(String se, SocketChannel sc) {
        /* Send the message to all users */
        messageAll("send", userData.getName(sc) + "," + se);
    }
    
    /**
     * This method is generic and will return an error to a user.
     */
    public void returnError(String err, SocketChannel sc) {
        /* Send the error message to the requested SocketChannel */
        message("fail", err, sc);
    }
    
    /** 
     * This method will send a message to a SocketChannel in the GOB protocol 
     * format.
     */
    private void message(String type, String msg, SocketChannel sc) {
        try {
            /* Write the message to the SocketChannel */
            sc.write(encoder.encode(CharBuffer.wrap("GOB:" + type + ":" + msg + "\n")));
        } catch (ClosedChannelException e) {    /* Is the SocketChannel closed? */
            /* Get the users name, based on the SocketChannel */
            String un = userData.getName(sc);
            
            /* Delete the user entry */
            userData.deleteEntry(sc);
            
            /* Let everyone logged in know the user has quit */
            messageAll("quit", un + "," + "Socket failed");
        } catch (Exception e) {
            /* Alert a different failure on the terminal */
            System.out.println("Error messaging socket: " + e);
        }
    }

    /**
     * This method is an extended form of message. It will notify _all_ signed in
     * users.
     */
    private void messageAll(String type, String msg) {
        /* Obtain a list of all SocketChannels */
        Object[] socketchannels = userData.listSockets();
            
        /* Cycle through each SocketChannel, and message each one in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            message(type, msg, (SocketChannel)socketchannels[loop]);
        }
    }
}