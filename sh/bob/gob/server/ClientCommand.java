/*
 * ClientCommand.java
 *
 * Created on 7 December 2002, 23:40
 */

package sh.bob.gob.server;

import sh.bob.gob.shared.TextValidation;
import sh.bob.gob.shared.TextInvalidException;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Pattern;


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
    
    /** 
     * Creates a new instance of ClientCommand.
     *
     * @param userdata UserData to use for instantiation.
     */
    public ClientCommand(UserData userdata) {
        /* Keep a local pointer to the user data object */
        userData = userdata;

        /* Setup charset,decoder and encoder for use by networking commands*/
        charset = Charset.forName("ISO-8859-1");
        decoder = charset.newDecoder();
        encoder = charset.newEncoder();
    }

    /** 
     * Responsible for the proper signup of new clients. 
     *
     * @param username Username of client
     * @param socketchannel SocketChannel of client
     */
    public void clientSignup(String username, SocketChannel socketchannel) {
        /* Check that the username is valid */
        try {
            TextValidation.isUserName(username);
        } catch(TextInvalidException ex) {
            /* Name is invalid, give error */
            returnError("Username supplied: " + ex, socketchannel);
            
            /* Notify on terminal */
            Main.consoleOutput("Attempt to sign-in with invalid username " +
                "(not shown) from: " + socketchannel.socket().getInetAddress().toString() +
                " problem is " + ex);
            return;
        }
        
        /* See if the user registration is valid */
        if(userData.insertName(username, socketchannel)) {
            /* Send a message to all users about the new user */
            /* messageAllExcept("signup", un, sc); */

            /* List all users currently logged in */
            clientRoomlist("*", socketchannel);
                
            /* Notify on the terminal that new user has signed up */
            Main.consoleOutput("New user signed in: \"" + username + "\"" + 
                " from " + userData.getHostIP(socketchannel));
        } else {
            /* Let the user know that there was an error with signup. */
            returnError("Already registered, or username taken", 
                socketchannel);

            /* Notify on the terminal about the new user */
            Main.consoleOutput("Attempt to sign in with duplicate " +
                "username: " + username);            
        }
    }

    /**
     * This method is responsible for the proper terminate of a user
     * when a quit command is submitted.
     */
    public void clientQuit(String pa, SocketChannel sc) {
        
        /* Make sure the quit reason is ... err ... reasonable */
        try {
            TextValidation.isQuitReason(pa);
        } catch (TextInvalidException ex) {
            /* Reason doesn't match correct criteria, just clear it */
            pa = "";
        }
        
        if(userData.isSocketRegistered(sc) == false) {
            /*The user never logged in, just log to console */
            Main.consoleOutput("An unknown user from: " + sc.socket().getInetAddress().toString() + " quit: " + pa);            
        }
        
        /* Get the username using the given SocketChannel */
        String userName = userData.getName(sc);

        //Main.consoleOutput("User quit: \"" + userName + "\" because \"" + pa + "\"");
            
        /* Let every room know that the user has quit */
        messageUserRooms("quit", userName, userName + "," + pa);
           
        /* We need to part every room */
        partAllRooms(userName);
            
        /* Remove the users entry from the UserData object */
        userData.deleteName(sc);
            
        /* Notify on the terminal that the user has quit */
        Main.consoleOutput("User quit: \"" + userName + "\" because \"" + pa + "\"");
            
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
    public void clientUserlist(String room, SocketChannel sc) {
        
        try {
            TextValidation.isRoomName(room);
        } catch (TextInvalidException ex) {
            returnError("Room name not valid", sc);
            
            return;
        }
        
        /* Obtain an array which contains a list of the current
          users in the room */
            
        if(userData.isRoomRegistered(room) != true) {
            returnError("Room name not registered [" + room + "]", sc);
            Main.consoleOutput("Userlist attempt on a non-registered room [" + room + "]");
            return;
        }
            
        Object[] users = userData.listNames(room);

        /* Parse the array of users, and create a single comma delimited
         * string */
        String list = new String();
        for(int loop = 0; loop <= (users.length -1); loop++) {
            list = list + users[loop];
            if(loop < (users.length -1)) {
                list = list + ",";
            }
        }           
        
        /* Return the list of users to the user who requested it */
        message("userlist", room + ":" + list, sc);
    }
    
    /**
     * Deal with requests for a user message to be sent.
     */
    public void clientRoomsend(String se, SocketChannel sc) {
        String[] Commands = se.split(":", 2);

        try {
            TextValidation.isRoomName(Commands[0]);
        } catch (TextInvalidException ex) {
            returnError("That room name is invalid: " + ex, sc);
            return;
        }

        try {
            TextValidation.isMessage(Commands[1]);
        } catch (TextInvalidException ex) {
            returnError("That message is invalid: " + ex, sc);
            return;
        }
        
        /* Check if the room is actually registered */
        if(userData.isRoomRegistered(Commands[0]) == false) {
            returnError("That room isn't registered", sc);
            return;
        }
        
        /* Send the message to all users */
        messageRoom("roomsend", Commands[0], userData.getName(sc) + ":" + Commands[1]);
    }
    
    /**
     * Send a user message to another user.
     */
    public void clientUsersend(String se, SocketChannel sc) {
        String[] Commands = se.split(":", 2);

        try {
            TextValidation.isUserName(Commands[0]);
        } catch (TextInvalidException ex) {
            returnError("The username is invalid: " + ex, sc);
            return;
        }
        
        try {
            TextValidation.isMessage(Commands[1]);
        } catch (TextInvalidException ex) {
            returnError("The message is invalid: " + ex, sc);
            return;
        }        
        
        /* Make sure the user exists */
        if(userData.isNameRegistered(Commands[0]) == false) {
            returnError("The user isn't logged on", sc);
            return;
        };
        
        /* Send the message to both the sender and originator */
        message("usersend", userData.getName(sc) + ":" + Commands[0] +
            ":" + Commands[1], userData.getSocket(Commands[0]));
        message("usersend", userData.getName(sc) + ":" + Commands[0] +
            ":" + Commands[1], sc);
    }
    
    /**
     * A user has joined a room
     */
    public void clientJoin(String room, SocketChannel sc) {
        /* Make sure the message to send is using good characters */
        try {
            TextValidation.isRoomName(room);
        } catch (TextInvalidException ex) {
            /* Reason doesn't match correct criteria, just clear it */
            returnError("The room you are requesting has invalid characters or is too long.", sc);
            return;
        }
        

        /* Join a room */
            
        /* If room doesn't exist, create it */
        if(userData.isRoomRegistered(room) == false) {
            userData.insertRoom(room);
                
            Main.consoleOutput("New room created: " + room);
        };
            
        /* Is the user already a member ? */
        if(userData.isMemberOf(room, userData.getName(sc)) == true) {
            returnError("Already a member of room \"" + room + "\".", sc);
            return;
        }
            
        /* If user isn't already a member, join it */
        Main.consoleOutput("The user: " + userData.getName(sc) + " is trying to join the room: " + room);
        userData.joinRoom(userData.getName(sc), room);
            
        /* Send a message to everyone in the room */
        messageRoom("join", room, userData.getName(sc));
            
    }
    
    /**
     * A user has left a room
     */
    public void clientPart(String room, SocketChannel sc) {
        /* Make sure the message to send is using good characters */
        try {
            TextValidation.isRoomName(room);
        } catch (TextInvalidException ex) {
            /* Reason doesn't match correct criteria, just clear it */
            returnError("The room you are attempting to part has invalid characters or is too long.", sc);            
            return;
        }

        /* Part a room */
            
        /* Does the room exist? */
        if(userData.isRoomRegistered(room) != true) {
            returnError("That room isn't registered: " + room, sc);
            return;
        }
            
        /* Send a message to everyone in the room */
        messageRoom("part", room, userData.getName(sc));
            
        /* Update the userdata bit */
        userData.partRoom(userData.getName(sc), room);
            
        /* If last in room, remove room */
        if(userData.listNames(room).length == 0) {
            userData.deleteRoom(room);
        }
    }
    
    /**
     * Return a list of rooms
     */
    public void clientRoomlist(String search, SocketChannel sc) {
        /* Make sure the criteria is correct */
        try {
            TextValidation.isSearch(search);
        } catch (TextInvalidException ex) {
            returnError("Invalid search criteria.",  sc);
            return;
        }

        /* Obtain an array which contains a list of the currently
         * registered rooms */
        Object[] rooms = userData.listRooms();

        /* Parse the array of rooms, and create a single comma delimited
         * string */
        String list = new String();
        for(int loop = 0; loop <= (rooms.length -1); loop++) {
            list = list + rooms[loop];
            if(loop < (rooms.length -1)) {
                list = list + ",";
            }
        }           
        
        /* Return the list of users to the user who requested it */
        message("roomlist", list, sc);
        
    }
    
    /**
     * Rename a user
     *
     */
    public void clientRename(String name, SocketChannel sc) {
        /* Check that the username is valid */
        try {
            TextValidation.isUserName(name);
        } catch (TextInvalidException ex) {
            /* Name is invalid, give error */
            returnError("I\'m sorry, usernames must be between 3 and 15 " +
                "characters and only alphanumeric", sc);
            
            /* Notify on terminal */
            Main.consoleOutput("Attempt to rename user to invalid username " +
                "(not shown) from \"" + sc.socket().getInetAddress().toString() + 
                "\" by user \"" + userData.getName(sc));

            return;
        }
        
        String oldname = userData.getName(sc);
            
        /* See if the user rename is valid */
        if(userData.renameName(oldname, name)) {
            /* Send a message to all users that can see this user about the rename */
            messageUsersInUserRooms("rename", userData.getName(sc), oldname + ":" + name);
            //messageUserRooms("rename", userData.getName(sc), oldname + ":" + se);
                
            /* Now message the original user */
            message("rename", oldname + ":" + name, sc);

            /* Notify on the terminal that new user has renamed */
            Main.consoleOutput("User renamed from \"" + oldname + "\"" + 
                " to \"" + name + "\"");
        } else {
            /* Let the user know that there was an error with rename. */
            returnError("Username taken", sc);

            /* Notify on the terminal about the new user */
            Main.consoleOutput("Attempt to rename to existing username \"" + name + "\" by \"" +
                userData.getName(sc) + "\"");
        }
        
    }
    
    /**
     * This method is generic and will return an error to a user.
     */
    public void returnError(String err, SocketChannel sc) {
        /* Send the error message to the requested SocketChannel */
        message("fail", err, sc);
    }
    
    /** 
     * This method will send a message to a SocketChannel in the GOB protocol 
     * format.
     */
    private void message(String type, String msg, SocketChannel sc) {
        //DEBUG
        //Main.consoleOutput("ClientMessage Type: " + type + " Msg: " + msg);
        try {
            /* Write the message to the SocketChannel */
            sc.write(encoder.encode(CharBuffer.wrap("GOB:" + type + ":" + msg + "\n")));
        } catch (IOException e) {    /* Is the SocketChannel closed? */
            /* Log it */
            Main.consoleOutput("Error messaging users socket, type: " + type + " msg: " + msg);
            /* Commented out due to an infinite loop, because clientQuit calls this routine */
//            clientQuit("Error messaging users socket.", sc);
            
        } 
    }

    /**
     * This method will message everyone in a room.
     */
    private void messageRoom(String type, String room, String msg) {
        /* Obtain a list of all socketChannels in specified room */
        Object[] socketchannels = userData.listSockets(room);
        
        /* Cycle through each SocketChannel, and message each on in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            message(type + ":" + room, msg, (SocketChannel)socketchannels[loop]);
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
    private void messageUsersInUserRooms(String type, String username, String msg) {
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
                message(type, msg, userData.getSocket((String)users[i]));
            }
        }
    }
    
    /**
     * This method will message each room a user belongs to.
     */
    private void messageUserRooms(String type, String username, String msg) {
        /* Check if username exists */
        if(userData.isNameRegistered(username) != true) {
            Main.consoleOutput("Attempt to message all userrooms for a username that doesn't exist");
            return;
        }
        
        /* Obtain a list of all rooms for username */
        Object[] rooms = userData.listRooms(username);
        
        /* Cycle through each room, and message each on in turn */
        for(int loop = 0; loop <= (rooms.length -1); loop++) {
            messageRoom(type, (String)rooms[loop], msg);
        }
    }

    
    /**
     * This method is an extended form of message. It will notify _all_ signed in
     * users.
     */
    private void messageAll(String type, String msg) {
        /* Obtain a list of all SocketChannels */
        Object[] socketchannels = userData.listSockets();
            
        /* Cycle through each SocketChannel, and message each one in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            message(type, msg, (SocketChannel)socketchannels[loop]);
        }
    }
    
    /**
     * This methods is the same as a messageAll, yet it skips the specified
     * exception.
     */
    private void messageAllExcept(String type, String msg, SocketChannel exception) {
        /* Obtain a list of all SocketChannels */
        Object[] socketchannels = userData.listSockets();

        /* Cycle through each SocketChannel, and message each one in turn */
        for(int loop = 0; loop <= (socketchannels.length -1); loop++) {
            /* Make sure we don't message the exception. Simple */
            if(socketchannels[loop].equals(exception)) {
                continue;
            } else {
                message(type, msg, (SocketChannel)socketchannels[loop]);
            }
        }
    }
    
    /**
     * This method will part each room a user belongs to.
     */
    private void partAllRooms(String username) {
        /* Obtain a list of all rooms for username */
        Object[] rooms = userData.listRooms(username);
        
        Main.consoleOutput("The user: " + username + " is parting all rooms");
        
        /* Cycle through each room, and part each in turn */
        for(int loop = 0; loop <= (rooms.length -1); loop++) {
            
            /* Update the userdata bit */
            userData.partRoom(username, (String)rooms[loop]);            
            
            //DEBUG
            //Main.consoleOutput("Check if the room: " + rooms[loop] + " needs to be closed, with users: " + userData.listNames((String)rooms[loop]).length);
            
            /* If last in room, remove room */
            if((userData.listNames((String)rooms[loop])).length == 0) {
                Main.consoleOutput("Now parting: " + rooms[loop]);
                userData.deleteRoom((String)rooms[loop]);
            }            
        }
    }
}