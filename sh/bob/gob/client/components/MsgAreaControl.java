/*
 * MsgAreaControl.java
 *
 * Created on 9 January 2003, 20:49
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

package sh.bob.gob.client.components;

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