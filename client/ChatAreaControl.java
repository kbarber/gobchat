/*
 * ChatAreaControl.java
 *
 * Created on 9 January 2003, 20:49
 */

package client;

import javax.swing.*;
import java.util.*;

/**
 * This object controls the main lobby JTextArea, provides mechanisms for outputting
 * messagse and status updates.
 *
 * @author  Ken Barber
 */
public class ChatAreaControl {
    
    private JTextArea chatArea;
    
    /** 
     * Creates a new instance of ChatAreaControl 
     *
     * @param ta The JTextArea this object will control
     */
    public ChatAreaControl(JTextArea ta) {
        chatArea = ta;
    }
    
    /**
     * This is a thread safe means to add a user message to the lobby
     * 
     * @param user The username of the message sender
     * @param msg The message sent
     */
    protected synchronized void userMessage(String user, String msg) {
        if(chatArea.getText().length() != 0) {
            chatArea.append("\n");
        }
        
        chatArea.append("<" + user + "> " + msg);
        
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    /**
     * This is a thread safe means to output a status message to the lobby.
     *
     * @param msg The status message to output
     */
    protected synchronized void statusMessage(String msg) {
        if(chatArea.getText().length() != 0) {
            chatArea.append("\n");
        }
        
        chatArea.append("- " + msg);
        
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    /**
     * Clear the text area.
     */
    protected synchronized void clearTextArea() {
        chatArea.setText(null);
    }
}
