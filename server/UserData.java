/*
 * UserData.java
 *
 * Created on 7 December 2002, 16:05
 */

package server;

import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class keeps the information regarding users currently logged in
 *
 * @author  Ken Barber
 */
public class UserData {
    
    private Hashtable hashNameSocket;    
    private Hashtable hashSocketName;

    private Hashtable hashNameRooms;
    private Hashtable hashRoomNames;
    
    /** 
     * Creates a new instance of UserData.
     */
    public UserData() {
        hashNameSocket = new Hashtable();
        hashSocketName = new Hashtable();
        hashNameRooms = new Hashtable();
        hashRoomNames = new Hashtable();
    }
    
    /** 
     * Insert new user data.
     */
    public boolean insertName(String name, SocketChannel socket) {
        /* Ensure these values don't already exist */
        if(hashNameSocket.containsKey(name) || hashSocketName.containsKey(socket)) {
            /* Return a failure if they do */
            return false;
        }
        
        /* Create duplicated entries */
        hashNameSocket.put(name, socket);
        hashSocketName.put(socket, name);
        hashNameRooms.put(name, new Hashtable());
        
        /* Success!! */
        return true;
    }
    
    /**
     * Insert new room data.
     *
     * @param room New room
     */
    public boolean insertRoom(String room) {
        /* Ensure the room doesn't already exist */
        if(hashRoomNames.containsKey(room)) {
            return false;
        }
        
        /* Create a new entry */
        hashRoomNames.put(room, new Hashtable());
        
        /* Success */
        return true;
    }
    
    /**
     * User joins room
     *
     * @param name Username to join
     * @param room Room to join
     */
    public void joinRoom(String name, String room) {
        Main.consoleOutput("Room: " + room + ", Name: " + name);
        
        /* Add the user to the room */
        ((Hashtable)hashRoomNames.get(room)).put(name, "dummy");
        
        /* Now the room to the user */
        ((Hashtable)hashNameRooms.get(name)).put(room, "dummy");
    }
    
    /**
     * User parts room
     *
     * @param name Username to part
     * @param room Room to part
     */
    public void partRoom(String name, String room) {
        /* Remove the user from the room */
        ((Hashtable)hashRoomNames.get(room)).remove(name);
        
        /* Now remove the room from the user */
        ((Hashtable)hashNameRooms.get(name)).remove(room);
    }
    
    /** 
     * Return the name associated with the socket parameter 
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
     * Return the hostname/IP associated with the socket parameter
     */
    public String getHostIP(SocketChannel socket) {
        return socket.socket().getInetAddress().toString();
    }

    /**
     * Return the hostname/IP associated with the name parameter
     */
    public String getHostIP(String name) {
        return ((SocketChannel)hashNameSocket.get(name)).socket().getInetAddress().toString();
    }
    
    
    /** 
     * Delete the entry with the username supplied.
     */
    public void deleteName(String name) {
        /* Now remove the references to this user in each room */
        String[] Rooms = (String[])listRooms(name);
        for(int i = 0; i < Rooms.length; i++) {
            ((Hashtable)hashRoomNames.get(Rooms[i])).remove(name);
        }
        
        hashSocketName.remove(getSocket(name));
        hashNameSocket.remove(name);     
        hashNameRooms.remove(name);
    }
    
    /** 
     * Delete the entry with the SocketChannel supplied.
     */
    public void deleteName(SocketChannel socket) {
        /* Now remove the references to this user in each room */
        Object[] Rooms = (Object[])listRooms(getName(socket));
        for(int i = 0; i < Rooms.length; i++) {
            ((Hashtable)hashRoomNames.get((String)Rooms[i])).remove(getName(socket));
        }
        
        hashNameRooms.remove(getName(socket));
        hashNameSocket.remove(getName(socket));
        hashSocketName.remove(socket);
    }
    
    /**
     * Delete the room specified.
     *
     * @param room Name of the room to delete
     */
    public void deleteRoom(String room) {
        /* Check if the room exists first */
        if(isRoomRegistered(room) != true) {
            return;
        }
        
        /* Now remove the references to the room for each user */
        Object[] Names = (Object[])listNames(room);
        for(int i = 0; i < Names.length; i++) {
            ((Hashtable)hashNameRooms.get((String)Names[i])).remove(room);
        }
        
        hashRoomNames.remove(room);
    }
    
    /**
     * See if this username is registered.
     */
    public boolean isNameRegistered(String name) {
        return hashNameSocket.containsKey(name);
    }
    
    /** 
     * See if this SocketChannel is registered.
     */
    public boolean isSocketRegistered(SocketChannel socket) {
        return hashSocketName.containsKey(socket);
    }
    
    /**
     * See if this Room is registered.
     *
     * @param room Room to check
     */
    public boolean isRoomRegistered(String room) {
        return hashRoomNames.containsKey(room);
    }
    
    /**
     * See if a user is a member of a room.
     *
     * @param user User to check
     * @param room Room to check
     */
    public boolean isMemberOf(String room, String user) {
        /* Check if the room exists first */
        if(isRoomRegistered(room) != true) {
            return false;
        }
        
        return ((Hashtable)hashRoomNames.get(room)).containsKey(user);
    }
    
    /**
     * Return an array of every username registered.
     */
    public Object[] listNames() {        
        return (hashNameSocket.keySet()).toArray();
    }
    
    /**
     * Return an array of every username in a particular room.
     * 
     * @param room Room to list users
     */
    public Object[] listNames(String room) {
        /* Check if the room exists first */
        if(isRoomRegistered(room) != true) {
            return null;
        }
        
        return ((Hashtable)hashRoomNames.get(room)).keySet().toArray();
    }
    
    /**
     * Return an array of every SocketChannel listed.
     */
    public Object[] listSockets() {
        return (hashSocketName.keySet()).toArray();
    }
    
    /**
     * Return an array of every SocketChannel in a room.
     *
     * @return Returns an Array of Sockets or null if the room doesn't exist
     * @param room Room to list from
     */
    public Object[] listSockets(String room) {
        /* Check if the room exists first */
        if(isRoomRegistered(room) != true) {
            return null;
        }
        
        Set RoomSet = ((Hashtable)hashRoomNames.get(room)).keySet();
        Object[] Names = RoomSet.toArray();
        Object[] Sockets = new Object[Names.length];
        
        for(int i = 0; i < Names.length; i++) {
            Sockets[i] = getSocket((String)Names[i]);
        }
        
        return Sockets;
    }
    
    /**
     * Return an array of every room registered
     *
     * @return An Array of rooms
     */
    public Object[] listRooms() {
        return (hashRoomNames.keySet()).toArray();
    }
    
    /**
     * Return an array of rooms that a particular user has joined
     *
     * @param name Username of interest
     * @return An Array of rooms or null if the user doesn't exist
     */
    public Object[] listRooms(String name) {
        /* Check if the user exists first */
        if(isNameRegistered(name) != true) {
            return null;
        }
        
        Set KeySet = ((Hashtable)hashNameRooms.get(name)).keySet();
        return KeySet.toArray();
    }
}
