/*
 * MsgAreaControl.java
 *
 * Created on 9 January 2003, 20:49
 */

package client.components;

import javax.swing.*;
import java.util.*;

/**
 * This object controls the main lobby JTextArea, provides mechanisms for outputting
 * messagse and status updates.
 *
 * @author  Ken Barber
 */
public class MsgAreaControl {
    
    private JTextArea chatArea;
    
    /** 
     * Creates a new instance of ChatAreaControl 
     *
     * @param ta The JTextArea this object will control
     */
    public MsgAreaControl(JTextArea ta) {
        chatArea = ta;
    }
    
    /**
     * This is a thread safe means to add a user message to the lobby
     * 
     * @param user The username of the message sender
     * @param msg The message sent
     */
    public synchronized void userMessage(String user, String msg) {
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
    public synchronized void statusMessage(String msg) {
        if(chatArea.getText().length() != 0) {
            chatArea.append("\n");
        }
        
        chatArea.append("- " + msg);
        
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    /**
     * Clear the text area.
     */
    public synchronized void clearTextArea() {
        chatArea.setText(null);
    }
}
