/*
 * ConnectionControl.java
 *
 * Created on 11 January 2003, 21:39
 */

package client;

import client.network.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

/**
 * This thread is responsible for connecting to the server, and dealing 
 * with any commands received.
 *
 * @author  Ken Barber
 */
public class ClientConnectionControl extends Thread {
    
    /* Pointers to GUIControl and Connection info */
    private ConnectionInfo conInfo;
    private GUIControl guiControl;
    
    /* The channel that we will use for connecting */
    private SocketChannel channel;
    
    /* The interrupt state */
    private boolean interruptState;
    
    private ServerConnectionThread scThread;
    private Thread threadInstance;
        
    /** 
     * Creates a new instance of ConnectionControl.
     *
     * @param ci ConnectionInfo object
     * @param ca ClientApplet object
     */
    public ClientConnectionControl(ClientApplet ca, ConnectionInfo ci) {
        conInfo = ci;
        guiControl = ca.guiControl;
        if(guiControl == null) {
            System.out.println("guiControl is null!");
        }
    }
    
    /**
     * Send a message to the server.
     *
     * @param message Message to send
     */
    public void sendMessage(String message) {
        scThread.sendMessage(message);
    }
    
    /**
     * Code to spawn a new connect thread, and connect to the server.
     */
    public void serverConnect(String username, String gobserver) {
        
        if(isAlive()) {
            System.out.println("The thread is alive, this probably won't work");
        }
        
        System.out.println("Attempt to spawn connection thread");
        
        /* Set connection info */
        conInfo.setUsername(username);
        conInfo.setServer(gobserver);
        
        System.out.println("Now fire up the thread");
        
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
     */
    public void serverDisconnect() {
        scThread.setInterrupt();
        threadInstance.interrupt();
    }

        

}
