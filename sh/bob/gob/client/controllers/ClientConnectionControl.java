/*
 * ClientConnectionControl.java
 *
 * Created on 11 January 2003, 21:39
 */

package sh.bob.gob.client.controllers;

import sh.bob.gob.client.network.*;
import sh.bob.gob.client.controllers.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

/**
 * This class is responsible for controlling the connection
 * thread, and being an interface for all the GUI panels.
 *
 * @author  Ken Barber
 */
public class ClientConnectionControl {
    
    /* Pointers to GUIControl and Connection info */
    private ConnectionInfo conInfo;
    private GUIControl guiControl;
    
    /* The channel that we will use for connecting */
    private SocketChannel channel;
    
    /* The interrupt state */
    private boolean interruptState;
    
    /* Thread vars */
    private ServerConnectionThread scThread;
    private Thread threadInstance;
        
    /** 
     * Creates a new instance of ConnectionControl.
     *
     * @param gc GUIControl object for interfacing with the GUI
     * @param ci ConnectionInfo object to work with
     */
    public ClientConnectionControl(GUIControl gc, ConnectionInfo ci) {
        conInfo = ci;
        guiControl = gc;
    }
    
    /**
     * Send a message to the server.
     *
     * @param message Message to send
     */
    public void sendCommand(String message) {
        scThread.sendCommand(message);
    }
    
    /**
     * Code to spawn a new connect thread, and connect to the server as the username
     * chosen.
     *
     * @param username The username to connect as
     * @param gobserver The host to connect to
     */
    public void serverConnect(String username, String gobserver) {
        
        /* Set connection info */
        conInfo.setUsername(username);
        conInfo.setServer(gobserver);
        
        /* Start thread */
        try {
            scThread = new ServerConnectionThread(guiControl,conInfo);
            threadInstance = new Thread(scThread);
            threadInstance.start();
            // start();
        } catch (Exception e) {
            System.out.println("Failure to start thread: " + e);
        }
    }
    
    /**
     * Code to interrupt the connection thread.
     *
     * @param message Disconnection message to pass to server
     */
    public void serverDisconnect(String message) {
        scThread.sendCommand("quit:" + message);
        guiControl.getGroupTabControl().removeAllGroups();
        guiControl.getPrivTabControl().removeAllUsers();
        scThread.setInterrupt();
        threadInstance.interrupt();
    }
    
    /**
     * Return the ConnectionInfo.
     *
     * @return The ConnectionInfo object
     */
    public ConnectionInfo getConnectionInfo() {
        return conInfo;
    };

}
