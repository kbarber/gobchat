/*
 * ClientCommand.java
 *
 * Created on 7 December 2002, 23:40
 */

package sh.bob.gob.server;

import sh.bob.gob.shared.validation.*;
import sh.bob.gob.shared.communication.*;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Pattern;
import java.util.logging.*;


/**
 * This object deals with all client commands.
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
    private MessageBox mbox;
    
    /** 
     * Creates a new instance of ClientCommand.
     *
     * @param userdata UserData to use for instantiation.
     */
    public ClientCommand(UserData userdata, MessageBox mb) {
        /* Keep a local pointer to the user data object */
        userData = userdata;
        mbox = mb;

        /* Setup charset,decoder and encoder for use by networking commands*/
        charset = Charset.forName("ISO-8859-1");
        decoder = charset.newDecoder();
        encoder = charset.newEncoder();
    }

    /** 
     * Responsible for the proper signup of new clients. 
     *
     * @param so SignOn databean
     * @param sc SocketChannel of client
     */
    public void clientSignup(SignOn so, SocketChannel sc) {
        String username = so.getUserName();
        
        /* Check that the username is valid */
        try {
            TextValidation.isUserName(username);
        } catch(TextInvalidException ex) {
            /* Name is invalid, give error */
            returnError(sc, so, "Username supplied: " + ex);
            
            /* Notify on terminal */
            Logger.getLogger("sh.bob.gob.server").log(Level.WARNING, "Attempt to sign-in with invalid username " +
                "(not shown) from: " + sc.socket().getInetAddress().toString(), ex);
            return;
        }
        
        /* See if the user registration is valid */
        if(userData.insertName(username, sc)) {
            /* Send a message to all users about the new user */
            /* messageAllExcept("signup", un, sc); */

            /* List all users currently logged in */
            RoomList rl = new RoomList();
            try { 
                rl.setFilter("*");
            } catch (TextInvalidException ex) {
            }
            clientRoomlist(rl, sc);
                
            /* Notify on the terminal that new user has signed up */
            Logger.getLogger("sh.bob.gob.server").fine("New user signed in: \"" + username + "\"" + 
                " from " + userData.getHostIP(sc));
        } else {
            /* Let the user know that there was an error with signup. */
            returnError(sc, so, "Already registered, or username taken");

            /* Notify on the terminal about the new user */
            Logger.getLogger("sh.bob.gob.server").fine("Attempt to sign in with duplicate " +
                "username: " + username);            
        }
    }

    /**
     * This method is responsible for the proper terminate of a user
     * when a quit command is submitted.
     */
    public void clientQuit(SignOff so, SocketChannel sc) {
        
        String pa = so.getMessage();
        
        /* Make sure the quit reason is ... err ... reasonable */
        try {
            TextValidation.isQuitReason(pa);
        } catch (TextInvalidException ex) {
            /* Reason doesn't match correct criteria, just clear it */
            pa = "";
        }
        
        if(userData.isSocketRegistered(sc) == false) {
            /*The user never logged in, just log to console */
            Logger.getLogger("sh.bob.gob.server").warning("An unknown user from: " + sc.socket().getInetAddress().toString() + " quit: " + pa);            
            
        } else {
        
            /* Get the username using the given SocketChannel */
            String userName = userData.getName(sc);

            /* Let every room know that the user has quit */
            so.setSuccess(true);
            messageUserRooms(userName, so);
           
            /* We need to part every room */
            partAllRooms(userName);
            
            /* Remove the users entry from the UserData object */
            userData.deleteName(sc);
            
            /* Notify on the terminal that the user has quit */
            Logger.getLogger("sh.bob.gob.server").fine("User quit: \"" + userName + "\" because \"" + pa + "\"");
        }
            
        try {
            /* Close the channel */
            sc.close();
        } catch(IOException e) {
            Main.programExit("Error closing socket: " + e);
        }

    }
    
    /**
     * Deal with a request to see the list of users.
     */
    public void clientUserlist(RoomUserList rul, SocketChannel sc) {
        
        String room = rul.getRoomName();
        
        try {
            TextValidation.isRoomName(room);
        } catch (TextInvalidException ex) {
            returnError(sc, rul, "Room name not valid");
            
            return;
        }
        
        /* Obtain an array which contains a list of the current
          users in the room */
            
        if(userData.isRoomRegistered(room) != true) {
            returnError(sc, rul, "Room name not registered [" + room + "]");
            Logger.getLogger("sh.bob.gob.server").warning("Userlist attempt on a non-registered room [" + room + "]");
            return;
        }
        
        Logger.getLogger("sh.bob.gob.server").finer("Userlist on room [" + room + "] by user [" + userData.getName(sc) + "]");
            
        Object[] users = userData.listNames(room);

        /* Parse the array of users, and create a single comma delimited
         * string */
        UserItem ui[] = new UserItem[users.length];
        for(int loop = 0; loop < (users.length); loop++) {
            try {
                ui[loop] = new UserItem();
                ui[loop].setUserName((String)users[loop]);
            } catch (TextInvalidException ex) {
                continue;
            }
        }           
        rul.setUsers(ui);
        
        /* Return the list of users to the user who requested it */
        rul.setSuccess(true);
        
        try {
            mbox.sendData(sc, rul);
        } catch (IOException ex) {
        }
    }
    
    /**
     * Deal with requests for a user message to be sent.
     */
    public void clientRoomsend(RoomSend rs, SocketChannel sc) {
        String[] Commands = {rs.getRoomName(), rs.getMessage()};

        try {
            TextValidation.isRoomName(Commands[0]);
        } catch (TextInvalidException ex) {
            returnError(sc, rs, "That room name is invalid: " + ex);
            return;
        }

        try {
            TextValidation.isMessage(rs.getMessage());
        } catch (TextInvalidException ex) {
            returnError(sc, rs, "That message is invalid: " + ex);
            return;
        }
        
        /* Check if the room is actually registered */
        if(userData.isRoomRegistered(Commands[0]) == false) {
            returnError(sc, rs, "That room isn't registered");
            return;
        }
        
        Logger.getLogger("sh.bob.gob.server").finer("Roomsend for room [" + 
            Commands[0] + "] by user [" + userData.getName(sc) + "] message [" + 
            Commands[1] + "]");
        
        /* Send the message to all users */
        try {
            rs.setUserName(userData.getName(sc));
        } catch (TextInvalidException ex) {
        }
        rs.setSuccess(true);
        messageRoom(rs.getRoomName(), rs);
    }
    
    /**
     * Send a user message to another user.
     */
    public void clientUsersend(UserMessage um, SocketChannel sc) {
        String[] Commands = {um.getUserNameDst(), um.getMessage()};

        try {
            TextValidation.isUserName(Commands[0]);
        } catch (TextInvalidException ex) {
            returnError(sc, um, "The username is invalid: " + ex);
            return;
        }
        
        try {
            TextValidation.isMessage(Commands[1]);
        } catch (TextInvalidException ex) {
            returnError(sc, um, "The message is invalid: " + ex);
            return;
        }        
        
        /* Make sure the user exists */
        if(userData.isNameRegistered(Commands[0]) == false) {
            returnError(sc, um, "The user isn't logged on");
            return;
        };
        
        Logger.getLogger("sh.bob.gob.server").finer ("Usersend from [" + 
            userData.getName(sc) + "] to [" + Commands[0] + "] message [" + 
            Commands[1] + "]");
        
        /* Send the message to both the sender and originator */
        um.setSuccess(true);
        try {
            mbox.sendData(sc,  um);
            mbox.sendData(userData.getSocket(um.getUserNameDst()), um);
        } catch (IOException ex) {
        }
    }
    
    /**
     * A user has joined a room
     */
    public void clientJoin(RoomJoin rj, SocketChannel sc) {
        String room = rj.getRoomName();
        
        /* Make sure the message to send is using good characters */
        try {
            TextValidation.isRoomName(room);
        } catch (TextInvalidException ex) {
            /* Reason doesn't match correct criteria, just clear it */
            returnError(sc, rj, "The room you are requesting has invalid characters or is too long.");
            return;
        }
        

        /* Join a room */
            
        /* If room doesn't exist, create it */
        if(userData.isRoomRegistered(room) == false) {
            userData.insertRoom(room);
                
            Logger.getLogger("sh.bob.gob.server").fine("New room created: " + room);
        };
            
        /* Is the user already a member ? */
        if(userData.isMemberOf(room, userData.getName(sc)) == true) {
            returnError(sc, rj, "Already a member of room \"" + room + "\".");
            return;
        }
            
        /* If user isn't already a member, join it */
        Logger.getLogger("sh.bob.gob.server").finer("The user: " + userData.getName(sc) + " is trying to join the room: " + room);
        userData.joinRoom(userData.getName(sc), room);
            
        /* Send a message to everyone in the room */
        rj.setSuccess(true);
        messageRoom(room, rj);
//        messageRoom("join", room, userData.getName(sc));
            
    }
    
    /**
     * A user has left a room
     */
    public void clientPart(RoomPart rp, SocketChannel sc) {
        String room = rp.getRoomName();
        
        /* Make sure the message to send is using good characters */
        try {
            TextValidation.isRoomName(room);
        } catch (TextInvalidException ex) {
            /* Reason doesn't match correct criteria, just clear it */
            returnError(sc, rp, "The room you are attempting to part has invalid characters or is too long.");            
            return;
        }

        /* Part a room */
            
        /* Does the room exist? */
        if(userData.isRoomRegistered(room) != true) {
            returnError(sc, rp, "That room isn't registered: " + room);
            return;
        }
        
        Logger.getLogger("sh.bob.gob.server").finer("Part from room [" + 
            room + "] by user [" + userData.getName(sc) + "]");
            
        /* Send a message to everyone in the room */
        rp.setSuccess(true);
        messageRoom(room, rp);
//        messageRoom("part", room, userData.getName(sc));
            
        /* Update the userdata bit */
        userData.partRoom(userData.getName(sc), room);
            
        /* If last in room, remove room */
        if(userData.listNames(room).length == 0) {
            userData.deleteRoom(room);
            Logger.getLogger("sh.bob.gob.server").fine("Room deleted: " + room);
        }
    }
    
    /**
     * Return a list of rooms
     */
    public void clientRoomlist(RoomList rl, SocketChannel sc) {
        String search = rl.getFilter();
        
        /* Make sure the criteria is correct */
        try {
            TextValidation.isSearch(search);
        } catch (TextInvalidException ex) {
            returnError(sc, rl, "Invalid search criteria.");
            return;
        }
        
        Logger.getLogger("sh.bob.gob.server").finer("Roomlist for [" + 
            search + "] by user [" + userData.getName(sc) + "]");

        /* Obtain an array which contains a list of the currently
         * registered rooms */
        Object[] rooms = userData.listRooms();

        /* Parse the array of rooms, and creates an array */
        RoomItem ri[] = new RoomItem[rooms.length];
        String list = new String();
        for(int loop = 0; loop <= (rooms.length -1); loop++) {
            try {
                ri[loop] = new RoomItem();
                ri[loop].setRoomName((String)rooms[loop]);
            } catch (TextInvalidException ex) {
                continue;
            }
        }
        
        rl.setRooms(ri);
        rl.setSuccess(true);
        
        /* Return the list of users to the user who requested it */
        try {
            mbox.sendData(sc, rl);
        } catch (IOException ex) {
            Logger.getLogger("sh.bob.gob.server").warning("IOException when returning list");
        }
        
    }
    
    /**
     * Rename a user
     *
     */
    public void clientRename(NameChange nc, SocketChannel sc) {
        String name = nc.getUserNameNew();
        
        /* Check that the username is valid */
        try {
            TextValidation.isUserName(name);
        } catch (TextInvalidException ex) {
            /* Name is invalid, give error */
            returnError(sc, nc, "I\'m sorry, usernames must be between 3 and 15 " +
                "characters and only alphanumeric");
            
            /* Notify on terminal */
            Logger.getLogger("sh.bob.gob.server").warning("Attempt to rename user to invalid username " +
                "(not shown) from \"" + sc.socket().getInetAddress().toString() + 
                "\" by user \"" + userData.getName(sc));

            return;
        }
        
        String oldname = userData.getName(sc);
            
        /* See if the user rename is valid */
        if(userData.renameName(oldname, name)) {
            /* Send a message to all users that can see this user about the rename */
            nc.setSuccess(true);
            messageUsersInUserRooms(userData.getName(sc), nc);
            //messageUserRooms("rename", userData.getName(sc), oldname + ":" + se);
                
            /* Now message the original user */
            
            try {
                mbox.sendData(sc, nc);
            } catch (IOException ex) {
            }

            /* Notify on the terminal that new user has renamed */
            Logger.getLogger("sh.bob.gob.server").fine("User renamed from \"" + oldname + "\"" + 
                " to \"" + name + "\"");
        } else {
            /* Let the user know that there was an error with rename. */
            returnError(sc, nc, "Username taken");

            /* Notify on the terminal about the new user */
            Logger.getLogger("sh.bob.gob.server").fine("Attempt to rename to existing username \"" + name + "\" by \"" +
                userData.getName(sc) + "\"");
        }
        
    }
    
    /**
     * This method is generic and will return an error to a user.
     */
    public void returnError(SocketChannel sc, DataBean databean, String err) {
        /* Send the error message to the requested SocketChannel */
        try {
            databean.setError(err);
        } catch (TextInvalidException ex) {
        }
        databean.setSuccess(false);
        
        try {
            mbox.sendData(sc, databean);
        } catch (IOException ex) {
        }
        
    }
    
    /**
     * This method will message everyone in a room.
     */
    private void messageRoom(String room, Object databean) {
        /* Obtain a list of all socketChannels in specified room */
        Object[] socketchannels = userData.listSockets(room);
        
        /* Cycle through each SocketChannel, and message each on in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            try {
                mbox.sendData((SocketChannel)socketchannels[loop], databean);
            } catch (IOException ex) {
                continue;
            }
        }
    }
    
    /** 
     * The method will message each user in each room, that a particular user belongs to.
     *
     * This will not send a message to the original user.
     *
     * @param type Type of the message
     * @param username The username in particular
     * @param msg Message to send
     */
    private void messageUsersInUserRooms(String username, Object databean) {
        /* Obtain a list of users rooms */
        Object[] rooms = userData.listRooms(username);
        
        /* Obtain list of users from rooms */
        Object[] users = userData.listNames(rooms);
        
        /* Go through each user and send each a message */
        for(int i = 0; i < users.length; i++) {
            if(username.equals((String)users[i])) {
                /* Don't send a message to the original user */
                continue;
            } else {
                try {
                    mbox.sendData(userData.getSocket((String)users[i]), databean);
                } catch (IOException ex) {
                }
            }
        }
    }
    
    /**
     * This method will message each room a user belongs to.
     */
    private void messageUserRooms(String username, Object databean) {
        /* Check if username exists */
        if(userData.isNameRegistered(username) != true) {
            Logger.getLogger("sh.bob.gob.server").warning("Attempt to message all userrooms for a username that doesn't exist");
            return;
        }
        
        /* Obtain a list of all rooms for username */
        Object[] rooms = userData.listRooms(username);
        
        /* Cycle through each room, and message each on in turn */
        for(int loop = 0; loop <= (rooms.length -1); loop++) {
            try {
                ((SignOff)databean).setRoomName((String)rooms[loop]);
            } catch (TextInvalidException ex) {
                Logger.getLogger("sh.bob.gob.server").warning("Invalid room name in listing");
                continue;
            }
            messageRoom((String)rooms[loop], databean);
        }
    }

    
    /**
     * This method is an extended form of message. It will notify _all_ signed in
     * users.
     */
    private void messageAll(String type, Object databean) {
        /* Obtain a list of all SocketChannels */
        Object[] socketchannels = userData.listSockets();
            
        /* Cycle through each SocketChannel, and message each one in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            try {
                mbox.sendData((SocketChannel)socketchannels[loop], databean);
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * This methods is the same as a messageAll, yet it skips the specified
     * exception.
     */
    private void messageAllExcept(SocketChannel exception, Object databean) {
        /* Obtain a list of all SocketChannels */
        Object[] socketchannels = userData.listSockets();

        /* Cycle through each SocketChannel, and message each one in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            /* Make sure we don't message the exception. Simple */
            if(socketchannels[loop].equals(exception)) {
                continue;
            } else {
                try {
                    mbox.sendData((SocketChannel)socketchannels[loop], databean);
                } catch (IOException ex) {
                }
            }
        }
    }
    
    /**
     * This method will part each room a user belongs to.
     */
    private void partAllRooms(String username) {
        /* Obtain a list of all rooms for username */
        Object[] rooms = userData.listRooms(username);
        
        Logger.getLogger("sh.bob.gob.server").finer("The user: " + username + " is parting all rooms");
        
        /* Cycle through each room, and part each in turn */
        for(int loop = 0; loop <= (rooms.length -1); loop++) {
            
            /* Update the userdata bit */
            userData.partRoom(username, (String)rooms[loop]);            
            
            //DEBUG
            Logger.getLogger("sh.bob.gob.server").finest("Check if the room: " + rooms[loop] + " needs to be closed, with users: " + userData.listNames((String)rooms[loop]).length);
            
            /* If last in room, remove room */
            if((userData.listNames((String)rooms[loop])).length == 0) {
                Logger.getLogger("sh.bob.gob.server").finer("Now parting: " + rooms[loop]);
                userData.deleteRoom((String)rooms[loop]);
            }            
        }
    }
    
}