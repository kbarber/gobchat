/*
 * ChatAreaControl.java
 *
 * Created on 9 January 2003, 20:49
 */

package client;

import javax.swing.*;
import java.util.*;

/**
 *
 * @author  Ken Barber
 */
public class ChatAreaControl {
    
    private JTextArea chatArea;
    
    /** Creates a new instance of ChatAreaControl */
    public ChatAreaControl(JTextArea ta) {
        chatArea = ta;
    }
    
    protected synchronized void userMessage(String user, String msg) {
        if(chatArea.getText().length() != 0) {
            chatArea.append("\n");
        }
        
        chatArea.append("<" + user + "> " + msg);
        
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    protected synchronized void statusMessage(String msg) {
        if(chatArea.getText().length() != 0) {
            chatArea.append("\n");
        }
        
        chatArea.append("- " + msg);
        
        chatArea.setCaretPosition(chatArea.getText().length());
    }
    
    protected synchronized void clearTextArea() {
        chatArea.setText(null);
    }
}
