/*
 * MsgAreaControl.java
 *
 * Created on 9 January 2003, 20:49
 */

package client.components;

import javax.swing.*;
import java.util.*;

/**
 * This object controls a chat JTextArea, provides mechanisms for outputting
 * messages and status updates.
 *
 * This object does not extend a JTextArea, it accepts an already available
 * JTextArea during construction.
 *
 * @author  Ken Barber
 */
public class MsgAreaControl {
    
    private JTextArea chatArea;
    
    /** 
     * Creates a new instance of ChatAreaControl 
     *
     * @param ta The JTextArea this MsgAreaControl will take control of
     */
    public MsgAreaControl(JTextArea ta) {
        chatArea = ta;
    }
    
    /**
     * This is a thread safe means to output a user message to the JTextArea
     * 
     * @param user The username of the message sender
     * @param msg The message to send to the JTextArea
     */
    public synchronized void userMessage(String user, String msg) {
        if(chatArea.getText().length() != 0) {
            chatArea.append("\n");
        }
        
        chatArea.append("<" + user + "> " + msg);
        
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    /**
     * This is a thread safe means to output a status message to the text area.
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
     * Clear the text area. Thread safe.
     */
    public synchronized void clearTextArea() {
        chatArea.setText(null);
    }
}