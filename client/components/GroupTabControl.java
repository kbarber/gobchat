/*
 * GroupTabControl.java
 *
 * Created on 12 October 2003, 18:57
 */

package client.components;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.awt.Component;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import client.panels.GroupChatPanel;
import client.controllers.GUIControl;
import client.controllers.ClientConnectionControl;

/**
 * This object is responsible for keeping track of the open group chat tabs
 * in the GUI.
 * 
 * This control is also responsible for opening and closing these tabs.
 *
 * Messages sent to the groups must also be sent through this interface.
 *
 * @author  ken
 */
public class GroupTabControl {
    
    private HashMap grouptabs;
    private GUIControl guicontrol;
    private ClientConnectionControl concontrol;
    private JTabbedPane tbMain;
    
    /** 
     * Creates a new instance of GroupTabControl
     *
     * @param gui GUIControl
     * @param ccc ClientConnectionControl
     * @param tb The JTabbedPane
     */
    public GroupTabControl(GUIControl gui, ClientConnectionControl ccc, JTabbedPane tb) {
        grouptabs = new HashMap();
        guicontrol = gui;
        concontrol = ccc;
        tbMain = tb;
    }
    
    /**
     * Add the group chat tab to the main tabbed pain.
     *
     * @param name The name of the group to create.
     */
    public void addGroup(String name) {
        if(isGroup(name) == false) {
            /* Create a new groupchatpanel */
            GroupChatPanel gcp = new GroupChatPanel(guicontrol, concontrol, name);
            
            /* Add the group to the hashmap */
            grouptabs.put(name, gcp);
            
            /* Add the group to the JTabbedPane */
            tbMain.addTab(name, gcp);
        }
    }
    
    /**
     * Remove the group chat tab from the main tabbed pane.
     *
     * @param name The name of the group to remove
     */
    public void removeGroup(String name) {
        if(isGroup(name) == true) {
            /* Now remove the tab from the tabbedpain */
            tbMain.remove((JPanel)grouptabs.get(name));
            
            /* Remove the group from the hashmap */
            grouptabs.remove(name);
        }
    }
    
    /**
     * Remove all groups from the main tabbed pane.
     */
    public void removeAllGroups() {
        Set groupset = grouptabs.keySet();
        Iterator groupsit = groupset.iterator();
        String groupname;
        
        /* Remove all panels from the JTabbedPane */
        while(groupsit.hasNext()) {
            groupname = (String)groupsit.next();
//        while((groupname = (String)groupsit.next()).equals(null) == false) {
            tbMain.remove((JPanel)grouptabs.get(groupname));
        }

        /* Now clear the HashMap */
        grouptabs.clear();
        
    }
    
    /**
     * Check weither the group exists already.
     *
     * @param name The name of the group to check
     */
    public boolean isGroup(String name) {
        return grouptabs.containsKey(name);
    }
}
