/*
 * ConnectionInfo.java
 *
 * Created on 11 January 2003, 21:30
 */

package client;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

/**
 *
 * @author  Ken Barber
 */
public class ConnectionInfo {
    
    private SocketChannel socketChannel;
    private String username;
    private String server;
    
    /** Creates a new instance of ConnectionInfo */
    public ConnectionInfo() {
    }
    
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
    
    public void setSocketChannel(SocketChannel sc) {
        socketChannel = sc;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String un) {
        username = un;
    }
    
    public String getServer() {
        return server;
    }
    
    public void setServer(String hostname) {
        server = hostname;
    }
    
}