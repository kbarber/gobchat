/*
 * UserData.java
 *
 * Created on 7 December 2002, 16:05
 */

package server;

import java.util.*;
import java.nio.channels.*;

/**
 *
 * @author  Ken Barber
 */
public class UserData {
    
    private Hashtable hashNameSocket;    
        
    private Hashtable hashSocketName;
    
    /** 
     * Creates a new instance of UserData.
     */
    public UserData() {
        hashNameSocket = new Hashtable();
        hashSocketName = new Hashtable();
    }
    
    /** 
     * Insert new user data.
     */
    public boolean insertData(String name, SocketChannel socket) {
        /* Ensure these values don't already exist */
        if(hashNameSocket.containsKey(name) || hashSocketName.containsKey(socket)) {
            /* Return a failure if they do */
            return false;
        }
        
        /* Create duplicated entries */
        hashNameSocket.put(name, socket);
        hashSocketName.put(socket, name);
        
        /* Success!! */
        return true;
    }
    
    /** 
     * Return the name assocaited with the socket parameter 
     */
    public String getName(SocketChannel socket) {
        return (String)hashSocketName.get(socket);
    }
    
    /** 
     * Return the socket associated with the name parameter 
     */
    public SocketChannel getSocket(String name) {
        return (SocketChannel)hashNameSocket.get(name);
    }
    
    /** 
     * Delete the entry with the username supplied.
     */
    public void deleteEntry(String name) {
        hashSocketName.remove(getSocket(name));
        hashNameSocket.remove(name);      
    }
    
    /** 
     * Delete the entry with the SoccketChannel supplied.
     */
    public void deleteEntry(SocketChannel socket) {
        hashNameSocket.remove(getName(socket));
        hashSocketName.remove(socket);
    }
    
    /**
     * See if this username is registered.
     */
    public boolean isRegistered(String name) {
        return hashNameSocket.containsKey(name);
    }
    
    /** 
     * See if this SocketChannel is registered.
     */
    public boolean isRegistered(SocketChannel socket) {
        return hashSocketName.containsKey(socket);
    }
    
    /**
     * Return an array of every username registered.
     */
    public Object[] listNames() {        
        return (hashNameSocket.keySet()).toArray();
    }
    
    /**
     * Return an array of every SocketChannel listed.
     */
    public Object[] listSockets() {
        return (hashSocketName.keySet()).toArray();
    }
}
