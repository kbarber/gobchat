/*
 * ConnectionControl.java
 *
 * Created on 29 December 2002, 18:12
 */

package server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

/**
 * This is the object responsible for accepting user connections and accepting all user commands.
 *
 * @author  Ken Barber
 */
public class ConnectionControl {
    
    /* The port for the server to listen on - this will eventually be dynamic. */
    private static final int serverPort = 6666;
    
    /* These are the pointers used for the server networking */
    private ServerSocketChannel listen_channel;
    private Selector listen_selector;
    
    /* References for the UserData and ClientCommand objects passed to us */
    private UserData userData;
    private ClientCommand clientCommand;
    
    /** 
     * Creates a new instance of ConnectionControl.
     */
    public ConnectionControl() {
        
        /* Prepare a ServerSocketChannel, and register it with a selector */
        listen_channel = readyServerSocketChannel();
        listen_selector = readySelector(listen_channel);
        
        /* This is the character encoding parts required by the networking commands */
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        
        /* Allocate buffers for receiving */
        ByteBuffer read_buffer = ByteBuffer.allocateDirect(1024);
        CharBuffer read_charbuffer = CharBuffer.allocate(1024);
        
        /* Create new instances of the UserData and ClientCommand objects */
        userData = new UserData();
        clientCommand = new ClientCommand(userData);
        
        /* This is the main networking loop, it will continue looping forever
         * continually accepting connections etc. */
        for(;;) {            
            
            /* Used for testing the select response */
            int selectResponse = 0;
            
            try {
                /* Wait until network activity is detected on any attached sockets */
                selectResponse = listen_selector.select(500);
            } catch(Exception e) {
                /* Log problems to the terminal */
                Main.consoleOutput("Problem with select: " + e);
            }
            
            /* If the selectResponse is greater than 0, than the selector has detected
             * activity on one of its registered SocketChannel's */
            if(selectResponse > 0) {
                /* Obtain Set of detected activities or Keys */
                Set readyKeys = listen_selector.selectedKeys();
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
                        /* Altert the problem on the terminal
                         *
                         * I don't actually know what would generate this condition */
                        Main.consoleOutput("Not valid!!!");
                    } else if(key.isAcceptable()) { /* We have received a new connection */
                        /* Prepare and register new connection with the Selector */
                        SocketChannel channel = readySocketChannel((ServerSocketChannel)key.channel());

                        /* Give GOB greeting */
                        try {
                            /* Send the message */
                            channel.write(encoder.encode(CharBuffer.wrap("GOB:greeting:Welcome to the server!\n")));
                            
                            /* Log new connection */
                            Main.consoleOutput("New connection from: " + channel.socket().getInetAddress().toString());
                        } catch(Exception e) {
                            /* Send errors to the terminal */
                            Main.consoleOutput("Error giving greeting: " + e);
                        }

                    } else if (key.isReadable()) {  /* Someone has sent a command */
                            
                        /* Reset all buffers */
                        read_buffer.clear();                        
                        read_charbuffer.clear();
                            
                        /* Get the SocketChannel assocaited with this Key */
                        SocketChannel socketchannel = (SocketChannel)key.channel();
                                        
                        /* Check to make sure this SocketChannel is actually connected */
                        if(!socketchannel.isConnected()) {
                            /* Altert to the problem on the terminal */
                            Main.consoleOutput("Its not connected!!!!");
                        }
                            
                        // readResult used for socketchannel read.
                        int readResult = 0;
                        
                        /* Read any pending data into a buffer */
                        try {
                            readResult = socketchannel.read(read_buffer);
                        } catch(Exception e) {
                            /* Output any problems to the terminal */
                            Main.consoleOutput("Error receiving data on network: " + e);
                        }
                            
                        /* See if we have received an end-of-stream */
                        if(readResult == -1) {
                            if(userData.isRegistered(socketchannel)) {
                                clientCommand.clientQuit("Client forcefully disconnected", socketchannel);
                            }
                        
                            try {
                                /* Close the channel */
                                socketchannel.close();
                            } catch(Exception e) {
                                Main.consoleOutput("Error closing EOS socket: " + e);
                            }
                                
                            /* Go the next next pending SelectionKey */
                            continue;
                        }

                        /* Flip the buffer */
                        read_buffer.flip();
                        
                        /* Decode the buffer using the character set specified above */
                        decoder.decode(read_buffer, read_charbuffer, false);
                        
                        /* Flip the buffer we have justed decoded */
                        read_charbuffer.flip();
                                               
                        /* Now convert this buffer to a string */
                        String read_string = read_charbuffer.toString();
                            
                        /* Ensure strings are of reasonable length */
                        if(read_string.length() < 6) {
                            /* Return an error to the user */
                            clientCommand.returnError("Invalid Command", socketchannel);
                            
                            /* Go to the next pending SelectionKey */
                            continue;
                        }

                        /* Remove line-break at the end of the String */
                        read_string = read_string.substring(0, read_string.length() -1);
                        
                        /* Remove carriage-return if there is one from the String */
                        if(read_string.substring(read_string.length() -1).equals("\r")) {
                            read_string = read_string.substring(0, read_string.length() -1);
                        }
                        
                        /* Break up string into command and value */
                        String command[] = read_string.split(":", 2);

                        /* All client commands should be have two parts */
                        if(command.length == 2) {

                            /* Ensure the command is reasonable, make sure it is
                             * alphabetical and between 3 and 15 characters 
                             */
                            if(Pattern.matches("[a-z]{3,15}", command[0]) == false) {
                                clientCommand.returnError("Command format incorrect", socketchannel);
                                continue;
                            }
                            
                            /* Deal with any command */
                            if(command[0].equals("signup")) { /* The command is a signup */
                                /* Signup the user */
                                clientCommand.clientSignup(command[1], socketchannel);
                            } else if(command[0].equals("quit")) { /* The command is quit */
                                /* If the user is signed in, inform all that the user
                                 * has now quit */
                                if(userData.isRegistered(socketchannel)) {     
                                    clientCommand.clientQuit(command[1], socketchannel);
                                }
                                
                                try {
                                    /* Attempt to close the SocketChannel */
                                    socketchannel.close();
                                } catch(Exception e) {
                                    /* Output errors to the terminal */
                                    Main.consoleOutput("Problem closing socket: " + e);
                                }
                                
                            } else if(command[0].equals("list")) { /* The command is list */
                                /* If the user is signed in, return a list of users */
                                if(userData.isRegistered(socketchannel)) {
                                        clientCommand.clientList(command[1], socketchannel);
                                } else { /* User isn't logged in */
                                    /* Return an error, informing the user he is not logged in */
                                    clientCommand.returnError("Not logged in", socketchannel);
                                }
                                
                            } else if(command[0].equals("send")) { /* The command is send */
                                /* If the user is signed in, send the user message */
                                if(userData.isRegistered(socketchannel)) {
                                    clientCommand.clientSend(command[1], socketchannel);
                                } else { /* User isn't logged in */
                                    /* Return an error, informing the user he is not logged in */
                                    clientCommand.returnError("Not logged in", socketchannel);
                                }
                                
                            } else {
                                /* Other commands are not recognised, so return an error */
                                clientCommand.returnError("Unknown command", socketchannel);
                                
                            }
                                
                        } else { /* The command did not have 2 parts */
                            /* Return an error */
                            clientCommand.returnError("Command format incorrect", socketchannel);
                                                                
                        }
                    }                        
                } /* While loop */
            } /* If test */
        } /* Networking infinite loop */
    }
    
    /** 
     * Prepares and returns a server socket channel. We register this with a selector. 
     */
    private ServerSocketChannel readyServerSocketChannel() {
        /* Declare a new ServerSocketChannel pointer, currently null */
        ServerSocketChannel channel = null;
        
        try {
            /* Open a new ServerSocketChannel */
            channel = ServerSocketChannel.open();
            
            /* Turn off blocking */
            channel.configureBlocking(false);
            
            /* Now bind this ServerSocketChannel to the correct TCP port */
            channel.socket().bind(new InetSocketAddress(serverPort));        
        } catch(Exception e) {
            /* Alert a problem on the terminal */
            Main.consoleOutput("readyServerSocketChannel: " + e);
        }
        
        /* Return the channel, which is now configured */
        return channel;
    }
    
    /** 
     * Prepares and returns a socket channel. We register this with a selector. 
     */
    private SocketChannel readySocketChannel(ServerSocketChannel sschannel) {
        /* Declare a new SocketChannel pointer, currently null */
        SocketChannel channel = null;
        
        try {
            /* Accept and configure connection */
            channel = sschannel.accept();
            channel.configureBlocking(false);
                        
            /* Register this SocketChannel with the selector, requesting that 
             * read operations will be the thing to look for */
            channel.register(listen_selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            /* Alert a problem on the terminal */
            Main.consoleOutput("readySocketChannel: " + e);
        }
        
        /* Return the channel, now accepted and registered to the selector */
        return channel;
    }
          
    /** 
     * Prepares and returns a selector. 
     */
    private Selector readySelector(ServerSocketChannel channel) {
        
        /* Create a new Selector pointer */
        Selector selector = null;
        
        try {
            /* Open a selector, and register the given ServerSocketChannel to it */
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            /* Alert a problem on the terminal */
            Main.consoleOutput("readySelector: " + e);
        }
        
        /* Return the selector, now with a registered ServerSocketChannel */
        return selector;
    }
    
}
