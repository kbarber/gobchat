/*
 * ConnectionControl.java
 *
 * Created on 29 December 2002, 18:12
 */

package sh.bob.gob.server;

import sh.bob.gob.shared.configuration.*;
import sh.bob.gob.shared.communication.*;
import sh.bob.gob.shared.validation.*;

import java.beans.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.logging.*;

/**
 * This is the object responsible for accepting user connections and accepting 
 * all user commands.
 *
 * @author  Ken Barber
 */
public class ConnectionControl {
    
    /**
     * The configuration of the server
     */
    private ServerConfiguration serverConfiguration;
    
    /** 
     * These are the pointers used for the server networking 
     */
    private ServerSocketChannel listenServerSC;
    private Selector listenSelector;
    
    /** 
     * References for the UserData and ClientCommand objects passed to us 
     */
    private UserData userData;
    private ClientCommand clientCommand;
    
    /**
     * Message box for XML communication.
     */
    private MessageBox mbox;
    
    /** 
     * Creates a new instance of ConnectionControl.
     *
     * @param sconf A ServerConfiguration Bean that specifies the loaded system configuration
     */
    public ConnectionControl(ServerConfiguration sconf) {
        
        /* 
         * The following code prepares all the necessary objects for 
         * network programming
         */
        
        serverConfiguration = sconf;
        
        int serverPort = sconf.getTCPPort();
        
        /* Prepare a ServerSocketChannel, and register it with a selector */
        listenServerSC = readyServerSocketChannel();
        listenSelector = readySelector(listenServerSC);
        
        /* This is the character encoding parts required by the networking commands */
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        
        /* Allocate buffers for receiving */
        ByteBuffer inputByteBuffer = ByteBuffer.allocateDirect(1024);
        CharBuffer inputCharBuffer = CharBuffer.allocate(1024);
        
        /* Create new instances of the UserData and ClientCommand objects */
        userData = new UserData();
        
        /* Create a new MessageBox for XML comms */
        mbox = new MessageBox(sconf.getNetwork());
        
        clientCommand = new ClientCommand(userData, mbox);
        
        /* 
         * This is the main networking loop, it will continue looping forever
         * continually accepting connections etc. 
         */
        for(;;) {            
            
            /* Used for testing the select response */
            int selectResponse = 0;
            
            /* Attempt a select */
            try {
                /* Wait until network activity is detected on any attached sockets */
                selectResponse = listenSelector.select(500);
            } catch(IOException e) {
                if(e.getMessage().equals("Interrupted system call")) {
                    /* This is usually a Ctrl-C */
                    Main.programExit("Program Interrupted");
                } else {
                    /* Log problems to the terminal */
                    Main.programExit("Problem with select: " + e);
                }
            }
            
            /* If the selectResponse is greater than 0, than the selector has detected
             * activity on one of its registered SocketChannel's */
            if(selectResponse > 0) {
                /* Obtain Set of detected activities or Keys */
                Set readyKeys = listenSelector.selectedKeys();
                Iterator readyItor = readyKeys.iterator();

                /* Cycle through each Key until the queue is empty */
                while(readyItor.hasNext()) {
                    /* Get this SelectionKey */
                    SelectionKey key = (SelectionKey)readyItor.next();

                    /* Remove this key from the Iterator, so we don't repeat 
                     * ourselves */
                    readyItor.remove();

                    /* Deal with the activity for the SelectionKey */
                    if(!key.isValid()) { /* The SelectionKey/SocketChannel is not valid */
                        /* Alert the problem on the terminal
                         *
                         * I don't actually know what would generate this condition */
                        Logger.getLogger("sh.bob.gob.server").warning("This SelectionKey isn\'t valid. This is unexpected behaviour.");
                    } else if(key.isAcceptable()) { /* We have received a new connection */
                        /* Prepare and register new connection with the Selector */
                        SocketChannel sc = readySocketChannel((ServerSocketChannel)key.channel());

                        /* Give GOB greeting */
                        Logger.getLogger("sh.bob.gob.server").finest("Sending welcome message");
                        try {
                            /* Send the message */
                            ServerMessage sm = new ServerMessage();
                            try {
                                sm = new ServerMessage (
                                    "Welcome to the server!"
                                    );
                            } catch (Exception ex) {
                                Logger.getLogger("sh.bob.gob.server").severe("Invalid server message");
                                
                                Main.programExit("Invalid server message");
                            }
                            
                            mbox.sendData(sc, sm);
                            
                            /* Log new connection */
                            Logger.getLogger("sh.bob.gob.server").fine("New connection from: " + sc.socket().getInetAddress().toString());
                        } catch(IOException e) {
                            /* Close the SocketChannel, if I can't write to it - then I don't want
                             * to continue */
                            try {
                                sc.close();
                                
                                /* Send errors to the terminal */
                                Logger.getLogger("sh.bob.gob.server").warning("Error giving welcoming greeting to a socket, it has been closed: " + e);
                            } catch(IOException ne) {
                                /* Send error to console and exit, I should be able to close an invalid socket */
                                Main.programExit("I am unable to close a socket that couldn\'t be written to in the first place: " + ne);
                            }
                        }
                        
                        userData.setSplitBuffer(sc, new SplitBuffer());
                        
                        System.out.print("Finished acceptable\n");
                    } else if (key.isReadable()) {  /* Someone has sent a command */
                            
                        /* Get the SocketChannel assocaited with this Key */
                        SocketChannel sc = (SocketChannel)key.channel();
                                        
                        /* Check to make sure this SocketChannel is actually connected */
                        if(!sc.isConnected()) {
                            /* Output the problem on the terminal */
                            Logger.getLogger("sh.bob.gob.server").warning("Received activity on a connected socket. However this socket isn\'t connected.");
                            
                            /* Close the socket, just in case */
                            try {
                                sc.close();
                            } catch(IOException e) {
                                Main.programExit("I\'ve attempted to close a socket, that was marked as not connected and it failed: " + e);
                            }
                            
                            /* Next selected key (if any) */
                            continue;
                        }
                            
                        Object databean[] = null;
                        
                        /* Read any pending data into a buffer */
                        try {
                            Logger.getLogger("sh.bob.gob.server").finest("Attempting to receive data on network");
                            databean = mbox.receiveData(sc, userData.getSplitBuffer(sc));
                            Logger.getLogger("sh.bob.gob.server").finest("Succeeded in receiving data");
                        } catch(IOException e) {
                            /* Output any problems to the terminal */
                            Logger.getLogger("sh.bob.gob.server").warning("Error receiving data on network: " + e);
                            
                            /* Close the channel */
                            try {
                                sc.close();
                            } catch(IOException ne) {
                                Main.programExit("I\'ve attempted to close a socket that I was having problems reading on: " + e);
                            }
                            
                            /* Next SelectedKey */
                            continue;
                        }
                        
                        if(databean == null) {
                            Logger.getLogger("sh.bob.gob.server").finest("Null databean");
                            SignOff so = new SignOff();
                            try {
                                so.setMessage("Client forcefully disconnected");
                            } catch (TextInvalidException ex) {
                            }
                            clientCommand.clientQuit(so, sc);
                            
                            /* DataBean wasn't read properly or end of file */
                            continue;
                        }
                        
                        userData.setSplitBuffer(sc, (SplitBuffer)databean[0]);
                            
                        Logger.getLogger("sh.bob.gob.server").finest("Length of databean array: " + databean.length);
                        
                        /* Cycle through each databean */
                        for(int i = 1; i < databean.length; i++) {
                            String objectName = databean[i].getClass().getName();
                            Object dataBean = databean[i];
                            String objectShortName = objectName.replaceAll("sh.bob.gob.shared.communication.","");
                            Logger.getLogger("sh.bob.gob.server").finer("Object Name: " + objectName);
                            Logger.getLogger("sh.bob.gob.server").finer("Object Short Name: " + objectShortName);
                            
                            /* Deal with any commands */
                            if(userData.isSocketRegistered(sc)) { /* Is the user registered yet? */
                                if(objectShortName.equals("RoomList")) { /* The command is roomlist */
                                    /* Return a list of rooms */
                                    clientCommand.clientRoomlist((RoomList)dataBean, sc);
                                } else if(objectShortName.equals("RoomUserList")) { /* The command is list */
                                    /* Return a list of users */
                                    clientCommand.clientUserlist((RoomUserList)dataBean, sc);
                                } else if(objectShortName.equals("RoomSend")) { /* The command is send */
                                    /* Send the user message */
                                    clientCommand.clientRoomsend((RoomSend)dataBean, sc);
                                } else if(objectShortName.equals("UserMessage")) { /* The command is usersend */
                                    /* Send the user message */
                                    clientCommand.clientUsersend((UserMessage)dataBean, sc);
                                } else if(objectShortName.equals("RoomJoin")) { /* The command is join */
                                    /* Join the desired room */
                                    clientCommand.clientJoin((RoomJoin)dataBean, sc);
                                } else if(objectShortName.equals("RoomPart")) {
                                    /* Part the desired room */
                                    clientCommand.clientPart((RoomPart)dataBean, sc);
                                } else if(objectShortName.equals("SignOff")) {
                                    /* Quit the server */
                                    clientCommand.clientQuit((SignOff)dataBean, sc);
                                } else if(objectShortName.equals("NameChange")) {
                                    /* Rename the user */
                                    clientCommand.clientRename((NameChange)dataBean, sc);
                                } else {
                                    /* Other commands are not recognised, so return an error */
                                    ServerMessage sm = new ServerMessage();
                                    try {
                                        sm.setMessage("Unknown command in this mode");
                                        mbox.sendData(sc, sm);
                                    } catch (Exception ex) { };
                                }
                            } else { /* Disconnected mode */
                                if(objectShortName.equals("SignOn")) { /* The command is a signup */
                                    /* Signup the user */
                                    clientCommand.clientSignup((SignOn)dataBean, sc);
                                        
                                } else if(objectShortName.equals("SignOff")) { /* The command is quit */
                                    /* Quit the user */
                                    clientCommand.clientQuit((SignOff)dataBean, sc);
                                
                                } else {
                                    /* Other commands are not recognised, so return an error */
                                    clientCommand.returnError(sc, (DataBean)dataBean, "Unknown command in this mode");
                                
                                }
                            }
                        }
                    } /* If test */
                } /* While loop */
            } /* If test */
        } /* Networking infinite loop */
    }
    
    /** 
     * Prepares and returns a server socket channel. We register this with a selector. 
     *
     * @return Returns a ServerSocketChannel to register
     */
    private ServerSocketChannel readyServerSocketChannel() {
        /* Declare a new ServerSocketChannel pointer, currently null */
        ServerSocketChannel ssc = null;
        
        try {
            /* Open a new ServerSocketChannel */
            ssc = ServerSocketChannel.open();
            
            /* Turn off blocking */
            ssc.configureBlocking(false);
            
            /* Now bind this ServerSocketChannel to the correct TCP port */
            ssc.socket().bind(new InetSocketAddress(serverConfiguration.getTCPPort()));        
        } catch(IOException e) {
            /* Alert a problem on the terminal and close the program */
            Main.programExit("Unable to ready ServerSocketChannel: " + e);
        }
        
        /* Return the channel, which is now configured */
        return ssc;
    }
    
    /** 
     * Prepares and returns a socket channel. We register this with a selector. 
     *
     * @param ssc The ServerSocketChannel with the waiting connection
     * @return Returns a SocketChannel after preparing and registration
     */
    private SocketChannel readySocketChannel(ServerSocketChannel ssc) {
        /* Declare a new SocketChannel pointer, currently null */
        SocketChannel sc = null;
        
        try {
            /* Accept and configure connection */
            sc = ssc.accept();

            /* Turn off blocking */
            sc.configureBlocking(false);
                        
            /* Register this SocketChannel with the selector, requesting that 
             * read operations will be the thing to look for */
            sc.register(listenSelector, SelectionKey.OP_READ);
        } catch (ClosedChannelException ex) {
            /* Alert a problem to the logger */
            Logger.getLogger("sh.bob.gob.server").log(Level.SEVERE,"Unable to ready SocketChannel, because its closed", ex);
        } catch (IOException e) {
            /* Alert a problem on the terminal */
            Logger.getLogger("sh.bob.gob.server").log(Level.SEVERE,"Unable to ready SocketChannel", e);
        }
 
        
        /* Return the channel, now accepted and registered to the selector */
        return sc;
    }
          
    /** 
     * Prepares and returns a selector. 
     *
     * <p>We open a selector using the Selector.open() static method, and then
     * register the selecter with the ServerSocketChannel.
     *
     * @param ssc The ServerSocketChannel to register with the selector
     * @return A Selector after registration with the given ServerSocketChannel
     */
    private Selector readySelector(ServerSocketChannel ssc) {
        
        /* Create a new Selector pointer */
        Selector selector = null;
        
        try {
            /* Open a selector, and register the given ServerSocketChannel to it */
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            /* For some reason the server socket channel has closed?? */
            Main.programExit("ServerSocketChannel was closed before I could register: " + e);
        } catch (IOException e) {
            /* Alert a problem on the terminal */
            Main.programExit("Problem opening selector: " + e);
        }
        
        /* Return the selector, now with a registered ServerSocketChannel */
        return selector;
    }
    
}


