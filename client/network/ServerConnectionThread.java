/*
 * ServerConnectionThread.java
 *
 * Created on 13 September 2003, 18:38
 */

package client.network;

import client.controllers.*;
import client.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

/**
 * This class is a thread class intended to connect to the Gob server and 
 * manage all communication.
 *
 * @author  ken
 */
public class ServerConnectionThread implements Runnable {
    
    private GUIControl guiControl;
    private ConnectionInfo connectionInfo;
    
    /* The channel that we will use for connecting */
    private SocketChannel channel;
    
    /* The interrupt state */
    private boolean interruptState;    
    
    /** Creates a new instance of ServerConnectionThread */
    public ServerConnectionThread(GUIControl gc, ConnectionInfo ci) {
        guiControl = gc;
        connectionInfo = ci;
    }
    
    /** 
     * Setup the thread to be interrupted.
     */
    public void setInterrupt() {
        interruptState = true;
    }
    
    /** 
     * Test if this thread has been set to be interrupted.
     */
    public boolean getInterrupt() {
        boolean tempState = interruptState;
        interruptState = false;
        return tempState;
    }
    
    /**
     * Send a command to the server.
     *
     * @param command Command to send
     */
    public void sendCommand(String command) {
        // This is the character encoding parts required
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        
        // Allocate buffers for receiving
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
        CharBuffer readCharBuffer = CharBuffer.allocate(1024);
        
        try {
            channel.write(encoder.encode(CharBuffer.wrap(command + "\n")));
            //guiControl.printError("sent command: " + command);
        } catch(Exception e) {
            guiControl.printError("Problem with sending command: " + e);
        }
    }
    
    public void run() {
        
        System.out.println("Starting connection thread.");
        
        /* Thread is not to be interrupted. */
        interruptState = false;

        /* Inform the user we are in the process of connecting */
        guiControl.setConnected(GUIControl.CONNECTING);

        /*
         * The following code prepares the network connectivity 
         */
        
        /* This is the character encoding parts required for the networking */
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        
        /* Allocate buffers for receiving */
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
        CharBuffer readCharBuffer = CharBuffer.allocate(1024);
        
        /* The selector for reception of server messages */
        Selector selector = null;

        /* Set the IP and port to connect to */
        InetSocketAddress sockAddress = new InetSocketAddress(connectionInfo.getServer(), 6666);
        
        /*
         * Now open a connection, signup and register it with a selector.
         */
        
        /* Open a connection and wait till it is connected */
        try {
            
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            
            channel.connect(sockAddress);

            for(int loop = 0; loop < 30; loop++) {
                
                // Wait if the channel is connected
                if(channel.finishConnect()) {
                    break;
                } else {
                    Thread.sleep(1000);
                }
            }
            
        } catch (Exception e) {
            guiControl.printError("Problem connecting: " + e);
            
            /* Update the control tab, we are disconnected */
            guiControl.setConnected(guiControl.DISCONNECTED, "Problem connecting");
            
            return;
        }
        
        /* Now signup */
        try {
            channel.write(encoder.encode(CharBuffer.wrap("signup:" + connectionInfo.getUsername() + "\n")));
        } catch (Exception e) {
            guiControl.printError("Problem with signup: " + e);
            
            /* Update the control tab, we are disconnected */
            guiControl.setConnected(guiControl.DISCONNECTED, "Problem signing up");
            
            return;
        }
        
        /* Now register this channel with a selector */
        try {
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            guiControl.printError("Problem with registering selector: " + e);
            
            /* Update the control tab, we are disconnected */
            guiControl.setConnected(guiControl.DISCONNECTED, "Selector error");
            
            return;
        }

        /* Inform the user we are connected */
        guiControl.setConnected(guiControl.CONNECTED);
            
        /* Todo: Need to have a better means of interrupting, or at least 
         * clean up this one */
        
        /* 
         * This loop is responsible for receiving the server messages
         * and updating the GUI.
         */
        while(getInterrupt() != true) {
            
            int selectResponse = 0;
            
            try {
                /* Wait until there is something to read */
                selectResponse = selector.select(1000);
            } catch (Exception e) {
                guiControl.printError("Problem with select: " + e);
            }
            
            if(selectResponse > 0) {
                /* Obtain set of actions */
                Set readyKeys = selector.selectedKeys();
                Iterator readyItor = readyKeys.iterator();

                /* Deal with each action */
                while(readyItor.hasNext()) {

                    /* Get the action key */
                    SelectionKey key = (SelectionKey)readyItor.next();

                    /* Remove this key from the set */
                    readyItor.remove();
                    
                    if (key.isReadable()) {  
                            
                        /* Resets buffers */
                        readBuffer.clear();                        
                        readCharBuffer.clear();
                            
                        /* Get channel */
                        SocketChannel socketchannel = (SocketChannel)key.channel();
                                        
                        if(!socketchannel.isConnected()) {
                            guiControl.printError("Socket not connected");
                        }
                            
                        /* Read in the buffer */
                        try {
                            if(socketchannel.read(readBuffer) == -1) {
                                /*
                                 * Deregister the socketchannel if end-of-stream.
                                 * Do something which makes sense for clients.
                                 */
                                socketchannel.close();
                                continue;
                            }
                        } catch(Exception e) {
                            guiControl.printError("Error receiving data on network: " + e);
                        }

                        readBuffer.flip();
                        decoder.decode(readBuffer, readCharBuffer,false);
                        readCharBuffer.flip();
                                               
                        String readString = readCharBuffer.toString();
                            
                        /* Ensure strings are of reasonable length */
                        if(readString.length() < 6) {
                            /* 
                             * If this happens, the server has sent an invalid command.
                             * Deal with it better than this!!
                             */
                            continue;
                        }

                        /* Remove line-break */
                        readString = readString.substring(0, readString.length() -1);
                        
                        /* Remove carriage-return if there is one */
                        if(readString.substring(readString.length() -1).equals("\r")) {
                            readString = readString.substring(0, readString.length() -1);
                        }
                        
                        /* Break up commands, because there may be many in the buffer */
                        String serverMsg[] = readString.split("\n");
                        
                        /* Cycle through each server message */
                        for(int loop = 0; loop < serverMsg.length; loop++) {
                                                
                            /* Break up string into command and value */
                            String command[] = serverMsg[loop].split(":", 3);
                            
                            /* All server commands have the GOB prefix ... */
                            if(command[0].equals("GOB")) {
                                if(command[1].equals("signup")) {
                                    /* Add this new user to the userlist */
                                    guiControl.addUser(command[2]);
                                    
                                    /* Print a status message */
                                    guiControl.statusMessage("New user \"" + command[2] + "\"");
                                } else if(command[1].equals("quit")) {
                                    /* Split the quit command, get the user and the reason */
                                    String params[] = command[2].split(",", 3);
                                    
                                    /* Print a status message */
                                    guiControl.statusMessage("User \"" + params[0] + "\" has disconnected because \"" + params[1] + "\"");
                                    
                                    /* Remove the user from the list */
//                                    guiControl.deleteUser(params[0]);
                                } else if(command[1].equals("list")) {
                                    /* Split the list of users */
                                    String users[] = command[2].split(",");
                                    
                                    /* Clear the list */
                                    guiControl.clearList();
                                    
                                    /* Now rebuild the list of users */
                                    for(int ind = 0; ind < users.length; ind++) {
                                        guiControl.addUser(users[ind]);
                                    }
                                } else if(command[1].equals("greeting")) {
                                    /* Send any greetings as a status message */
                                    guiControl.statusMessage(command[2]);
                                } else if(command[1].equals("send")) {
                                    /* Split the send command, get the user and the message */
                                    String param[] = command[2].split(",", 2);
                                    
                                    /* Display the user message in the GUI */
                                    guiControl.userMessage(param[0], param[1]);
                                } else if(command[1].equals("fail")) {
                                    /* Send the error */
                                    guiControl.statusMessage("Server: " + command[2]);
                                } else if(command[1].equals("roomlist")) {
                                    /* Load the list control with the list of rooms */
                                    
                                    /* Split the list, and put it in an array (or compat. format) */
                                    String rooms[] = command[2].split(",");
                                    
                                    /* Load the array into the list control in the Room List tab */
                                    guiControl.setRoomList(rooms);
                                } else if(command[1].equals("join")) {
                                    /* split the parameters - room:user */
                                    String params[] = command[2].split(":");
                                    
                                    if(params[1].equals(connectionInfo.getUsername())) {
                                        /* If the user is us, we have joined a room, create a new group panel */
                                        guiControl.getGroupTabControl().addGroup(params[0]);
                                    } else {
                                        /* If the user is someone else, they have joined the room, update
                                         * the rooms user list */
                                    }
                                    
                                } else {
                                    /* Just send any received messages to the textarea for now */
                                    guiControl.statusMessage(serverMsg[loop]);
                                }
                            }
                        }
                    }
                }
            }
        }

        /* Attempt to close the connection, wait till it is closed */
        try {
            selector.close();
            channel.close();
            
            /* Inform the user we are disconnecting */
            guiControl.setConnected(guiControl.DISCONNECTING);
            
            for(int loop = 0; loop < 120; loop++) {
                if(channel.isConnected()) {
                    Thread.sleep(500);
                } else {
                    break;
                }
            }
        } catch(Exception e) {
            guiControl.printError("Problem disconnecting: " + e);
        }
        
        /* Put a message in the text area, stating we have disconnected */
        guiControl.statusMessage("Disconnected\n");
        
        /* Update the control tab, we are disconnected */
        guiControl.setConnected(guiControl.DISCONNECTED, "User disconnected");
        
    }
    
}
