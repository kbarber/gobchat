/*
 * ClientConnectionControl.java
 *
 * Created on 11 January 2003, 21:39
 */

/*
    Copyright (C) 2003,2004 Ken Barber
 
    This file is part of Gob Online Chat.

    Gob Online Chat is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    any later version.

    Gob Online Chat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Gob Online Chat; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package sh.bob.gob.client.controllers;

import sh.bob.gob.client.network.*;
import sh.bob.gob.client.controllers.*;
import sh.bob.gob.shared.communication.*;
import sh.bob.gob.shared.validation.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import java.util.logging.*;

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
    
    /* Is the server connected? */
    private boolean connectionState;
        
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
     * Sends a communication Bean of data to the server.
     *
     * @param obj Bean to send
     */
    public void sendData(Object obj) {
        scThread.sendData(obj);
    }
    
    /**
     * Send a message to the server.
     *
     * @param message Message to send
     */
//    public void sendCommand(String message) {
//        scThread.sendCommand(message);
//    }
    
    /**
     * Code to spawn a new connect thread, and connect to the server as the username
     * chosen.
     *
     * @param username The username to connect as
     * @param gobserver The host to connect to
     */
    public void serverConnect(String username, String gobserver) {
        if(connectionState == true) {
            return;
        }
        
        /* Set connection info */
        conInfo.setUsername(username);
        conInfo.setServer(gobserver);
        
        /* Start thread */
        try {
            scThread = new ServerConnectionThread(guiControl,conInfo);
            threadInstance = new Thread(scThread);
            threadInstance.start();
            connectionState = true;
        } catch (Exception ex) {
            Logger.getLogger("sh.bob.gob.client").severe("Failure to start thread: " + ex);
        }
    }
    
    /**
     * Code to interrupt the connection thread.
     *
     * @param message Disconnection message to pass to server
     */
    public void serverDisconnect(String message) {
        Logger.getLogger("sh.bob.gob.client").info("Disconnecting.");
        
        if(connectionState == false) {
            return;
        }
        
        SignOff so = new SignOff();
        try {
            so.setMessage(message);
        } catch (TextInvalidException ex) {
            guiControl.statusMessage("Invalid disconnection message: " + ex);
        }
        
        try {
            so.setUserName(getConnectionInfo().getUsername());
            sendData(so);
        } catch (TextInvalidException ex) {
            guiControl.statusMessage("Invalid username: " + ex);
        }
        
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
