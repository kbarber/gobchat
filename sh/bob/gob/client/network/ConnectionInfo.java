/*
 * ConnectionInfo.java
 *
 * Created on 11 January 2003, 21:30
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