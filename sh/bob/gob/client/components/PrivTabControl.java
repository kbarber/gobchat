/*
 * PrivTabControl.java
 *
 * Created on 12 October 2003, 18:57
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

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import sh.bob.gob.client.panels.PrivChatPanel;
import sh.bob.gob.client.controllers.GUIControl;
import sh.bob.gob.client.controllers.ClientConnectionControl;

/**
 * This object is responsible for keeping track of the open private chat tabs
 * in the GUI.
 * 
 * This control is also responsible for opening and closing these tabs.
 *
 * Messages sent to users must also be sent through this interface.
 *
 * @author  ken
 */
public class PrivTabControl {
    
    private HashMap privtabs;
    private GUIControl guicontrol;
    private ClientConnectionControl concontrol;
    private JTabbedPane tbMain;
    
    /** 
     * Creates a new instance of PrivTabControl
     *
     * @param gui GUIControl
     * @param ccc ClientConnectionControl
     * @param tb The JTabbedPane
     */
    public PrivTabControl(GUIControl gui, ClientConnectionControl ccc, JTabbedPane tb) {
        privtabs = new HashMap();
        guicontrol = gui;
        concontrol = ccc;
        tbMain = tb;
    }
    
    /**
     * Add the user chat tab to the main tabbed pain.
     *
     * @param name The name of the group to create.
     */
    public void addUser(String name) {
        if(isUser(name) == false) {
            /* Create a new groupchatpanel */
            PrivChatPanel pcp = new PrivChatPanel(guicontrol, concontrol, name);
            
            /* Add the group to the hashmap */
            privtabs.put(name, pcp);
            
            /* Add the group to the JTabbedPane */
            tbMain.addTab(name, pcp);
            
            /* Now focus on the tab */
            tbMain.setSelectedComponent(pcp);
        }
    }

    /**
     * Remove the user chat tab from the main tabbed pane.
     *
     * @param name The name of the user to remove
     */
    public void removeUser(String name) {
        if(isUser(name) == true) {
            /* Now remove the tab from the tabbedpain */
            tbMain.remove((JPanel)privtabs.get(name));
            
            /* Remove the group from the hashmap */
            privtabs.remove(name);
        }
    }
    
    /**
     * Remove all users from the main tabbed pane.
     */
    public void removeAllUsers() {
        Set userset = privtabs.keySet();
        Iterator usersit = userset.iterator();
        String username;
        
        /* Remove all panels from the JTabbedPane */
        while(usersit.hasNext()) {
            username = (String)usersit.next();
            tbMain.remove((JPanel)privtabs.get(username));
        }

        /* Now clear the HashMap */
        privtabs.clear();
        
    }
    
    /**
     * Check weither the user exists already.
     *
     * @param name The name of the user to check
     * @return A boolean, true if the group exists, false if not
     */
    public boolean isUser(String name) {
        return privtabs.containsKey(name);
    }
    
    /**
     * Write a user message to the user panel
     *
     * @param namesrc Source user
     * @param namedst Destination user
     * @param message Message to be sent
     */
    public void writeUserMessage(String namesrc, String namedst, String message) {
        ((PrivChatPanel)privtabs.get(namesrc)).writeUserMessage(namedst, message);
    }    
    
    /**
     * Write a status message to the room panel.
     *
     * @param user Name of room
     * @param message Message to be sent
     */
    public void writeStatusMessage(String user, String message) {
        ((PrivChatPanel)privtabs.get(user)).writeStatusMessage(message);
    }
    
    /** 
     * Return a list of users.
     *
     * @return An array of objects (strings) representing a list of all users.
     */
    public Object[] getUserList() {
        return privtabs.keySet().toArray();
    }
}
