/*
 * ConnectionInfo.java
 *
 * Created on 11 January 2003, 21:30
 */

package sh.bob.gob.client.network;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

/**
 * Current client connection information is stored here
 *
 * @author  Ken Barber
 */
public class ConnectionInfo {
    
    private SocketChannel socketChannel;
    private String username;
    private String server;
    
    /** 
     * Creates a new instance of ConnectionInfo 
     */
    public ConnectionInfo() {
    }
    
    /**
     * Returns the socket channel we are currently using.
     */
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
    
    /**
     * Sets the socket channel we are currently using.
     */
    public void setSocketChannel(SocketChannel sc) {
        socketChannel = sc;
    }
    
    /**
     * Return the current username.
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the current username.
     *
     * @param un Username to set
     */
    public void setUsername(String un) {
        username = un;
    }
    
    /**
     * Gets the current server.
     */
    public String getServer() {
        return server;
    }
    
    /**
     * Sets the current server
     *
     * @param hostname Hostname of server
     */
    public void setServer(String hostname) {
        server = hostname;
    }
    
}