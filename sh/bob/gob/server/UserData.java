/*
 * UserData.java
 *
 * Created on 7 December 2002, 16:05
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

package sh.bob.gob.server;

import sh.bob.gob.shared.communication.*;

import java.nio.channels.*;
import java.util.logging.*;
import java.util.*;

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
    
    private Hashtable hashSocketSplitBuffer;
    
    /** 
     * Creates a new instance of UserData.
     */
    public UserData() {
        hashNameSocket = new Hashtable();
        hashSocketName = new Hashtable();
        hashNameRooms = new Hashtable();
        hashRoomNames = new Hashtable();
        hashSocketSplitBuffer = new Hashtable();
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
        
        /* SplitBuffer setup */
        SplitBuffer sb = new SplitBuffer();
        sb.setSplitBuffer(null);
        hashSocketSplitBuffer.put(socket, sb);
        
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
        Logger.getLogger("sh.bob.gob.server").finest("Join Room: " + room + " Name: " + name);
        
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
     * Get the buffer from the previously split packet.
     *
     * @param socket Socket to query
     * @return An array of bytes
     */
    public SplitBuffer getSplitBuffer(SocketChannel socket) {
        return (SplitBuffer)hashSocketSplitBuffer.get(socket);
    }
    
    /**
     * Set split buffer.
     *
     * @param socket Socket to enact set
     * @param inbuffer An array of bytes to put in the buffer
     */
    public void setSplitBuffer(SocketChannel socket, SplitBuffer inbuffer) {
        inbuffer.setTimeStamp(new Long(new Date().getTime()));
        hashSocketSplitBuffer.put(socket, inbuffer);
    }
    
    /**
     * Rename the user to something new.
     *
     * @param oldname Old name of user
     * @param newname New name of user
     * @return Returns a boolean, true if the rename is fine, false if the username is taken
     */
    public boolean renameName(String oldname, String newname) {
        /* Lets check if the username is taken */
        if(isNameRegistered(newname) == true) {
            /* Sorry, name taken */
            return false;
        };
        
        /* Grab the socket channel of the user */
        SocketChannel sc = getSocket(oldname);
        
        /* Grab the list of rooms the user was in */
        Object[] rooms = listRooms(oldname);
        
        /* Delete the name */
        deleteName(oldname);
        
        /* Create the name */
        insertName(newname, sc);
        
        /* Now the user must join all the groups they were once in */
        for(int i = 0; i < rooms.length; i++) {
            joinRoom(newname, (String)rooms[i]);
        }
        
        /* All good */
        return true;
    }
    
    
    /** 
     * Delete the entry with the username supplied.
     *
     * @param name Username to delete
     */
    public void deleteName(String name) {
        /* Now remove the references to this user in each room */
        Logger.getLogger("sh.bob.gob.server").finest("Delete user: " + name);
        
        Object[] Rooms = (Object[])listRooms(name);
        
        for(int i = 0; i < Rooms.length; i++) {
            ((Hashtable)hashRoomNames.get((String)Rooms[i])).remove(name);
        }
        
        hashSocketName.remove(getSocket(name));
        hashSocketSplitBuffer.remove(hashNameSocket.get(name));
//        hashSocketSplitBufferTS.remove(hashNameSocket.get(name));
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
        hashSocketSplitBuffer.remove(socket);
//        hashSocketSplitBufferTS.remove(socket);
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
     * Return an array of every username in a list of rooms.
     *
     * @param rooms Array of String for names
     * @return Array of Objects representing Strings of users
     */
    public Object[] listNames(Object[] rooms) {
        Hashtable userHash = new Hashtable();
        
        /* Run through each room */
        for(int i = 0; i < rooms.length; i++ ) {
            String room = (String)rooms[i];
            
            Object[] users = listNames(room);
            
            for(int l = 0; l < users.length; l++ ) {
                userHash.put(users[l], "dummy");
            }
            
        }
        
        return userHash.keySet().toArray();
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
            Logger.getLogger("sh.bob.gob.server").warning("Invalid listRooms for a name not registered: " + name);
            return null;
        }
        
        Set KeySet = ((Hashtable)hashNameRooms.get(name)).keySet();
        return KeySet.toArray();
    }
}
