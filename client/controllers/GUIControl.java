/*
 * GUIControl.java
 *
 * Created on 11 January 2003, 22:13
 */

package client.controllers;

import client.components.*;
import client.panels.*;

import javax.swing.*;
import java.util.*;

/**
 * This class builds the GUI and provides an interface for control.
 *
 * @author  Ken Barber
 */
public class GUIControl {
    
    private String hostname;
    private ListControl ulControl;
    private ListControl roomListControl;
    private MsgAreaControl saControl;
    private JButton bConnect;
    private JButton bDisconnect;
    private JLabel lConnectionStatus;
    private JTextArea taErrorOutput;
    private JTabbedPane tbMain;
    private JTextField tfUserName;
    private JTextField tfSendPrep;
    private ControlPanel pControl;
    private StatusPanel pStatus;
    private GroupChatPanel pLobby;
    private RoomListPanel pRoomList;
    private ClientConnectionControl conControl;
    private GroupTabControl groupTabControl;
    private PrivTabControl privTabControl;
    
    private int connectionStatus;
    
    public static int CONNECTING = 1;
    
    public static int CONNECTED = 2;
    
    public static int DISCONNECTING = 3;
    
    public static int DISCONNECTED = 4;
    
    /** 
     * Creates a new instance of GUIControl 
     *
     * @param tbm Main tabbed pane provided in the applet
     * @param hn Hostname to connect to
     */
    public GUIControl(JTabbedPane tbm, String hn) {
        tbMain = tbm;
        hostname = hn;
        
    }
    
    /**
     * This method will start the display of the GUI
     *
     * @param cc Control interface for networking and connection
     */
    public void displayGUI(ClientConnectionControl cc) {
        conControl = cc;
        
        /* Add a control and status panel */
        
        pControl = new ControlPanel(this, conControl, hostname);
        pStatus = new StatusPanel();
        //pLobby = new GroupChatPanel(this, conControl);
        pRoomList = new RoomListPanel(this, conControl);
        
        tbMain.addTab("Control", null, pControl, "For connecting and changing username");
        tbMain.addTab("Status", null, pStatus, "A log of any server or client error messages");
        //tbMain.addTab("Room List", null, pRoomList, "A list of all open rooms");
                
        /* Setup both controls for the ChatArea and UserList */
        saControl = new MsgAreaControl(pStatus.taErrorOutput);
        //ulControl = new ListControl(pLobby.lUsers);
        roomListControl = new ListControl(pRoomList.lRooms);
        
        /* Create a new GroupTabControl for creation of groups */
        groupTabControl = new GroupTabControl(this,  conControl, tbMain);
        
        /* Create a new PrivTabControl for creation of users */
        privTabControl = new PrivTabControl(this, conControl, tbMain);
        
    }
    
    /**
     * Set connection status
     *
     * @param status Status (from GUIControl static methods) to change to
     */
    public void setConnected(int status) {
        setConnected(status, "No reason");
    }
    
    /**
     * Set connection status
     *
     * @param status Status (from GUIControl static methods) to change to
     * @param reason Reason for status change
     */
    public void setConnected(int status, String reason) {
        
        connectionStatus = status;
        
        switch(status) {
            case 1: // Connecting
                pControl.bConnect.setEnabled(false);
                pControl.bDisconnect.setEnabled(true);                
                pControl.lConnectionStatus.setText("Connecting ...");
                
                /* And disable changing the username field */
                pControl.tfUserName.setEditable(false);
                break;
            case 2: // Connected
                pControl.bConnect.setEnabled(false);
                pControl.bDisconnect.setEnabled(true);     
                pControl.lConnectionStatus.setText("Connected");
                
                /* Allow renaming of user */
                pControl.bRename.setEnabled(true);
                pControl.tfNewUserName.setEditable(true);
                
                /* Add the lobby tab to the tabbed pain */
                tbMain.addTab("Room List", null, pRoomList, "A list of all open rooms");
                
                /* Now open the lobby tab */
                tbMain.setSelectedIndex(2);
                
                break;
            case 3: // Disconnecting   
                pControl.bConnect.setEnabled(true);
                pControl.bDisconnect.setEnabled(false);
                pControl.lConnectionStatus.setText("Disconnecting ...");
                break;
            case 4: // Disconnected
                pControl.bConnect.setEnabled(true);
                pControl.bDisconnect.setEnabled(false);
                
                /* Disallow renaming of user */
                pControl.bRename.setEnabled(false);
                pControl.tfNewUserName.setEditable(false);
                
                /* Set a reason for disconnection */
                pControl.lConnectionStatus.setText("Disconnected: " + reason);
                
                /* And enable the username field */
                pControl.tfUserName.setEditable(true);
                
                /* And now request focus if this tab is open */
                pControl.tfUserName.requestFocusInWindow();
                
                /* Now remove the lobby */
                tbMain.removeTabAt(2);
                break;
        }
    }
    
    /**
     * Obtain the connection status
     */
    public int getConnected() {
        return connectionStatus;
    }
    
    /**
     * Print an error to the status panel
     *
     * @param output Error to output to the status panel text area
     */
    public void printError(String output) {
        pStatus.taErrorOutput.append(new Date().toString() + ": " + output + "\n");
    }
    
    /**
     * Send a status message 
     *
     * @param output Message to send
     */
    public void statusMessage(String output) {
        saControl.statusMessage(output);
    }
    
    /**
     * Send a usermessage to the chosen chat panel
     *
     * @param un Username to send from
     * @param output Message contents
     */
    public void userMessage(String un, String output) {
        saControl.userMessage(un, output);
    }
    
    /**
     * Clear the rooms list in the room list panel.
     */
    public void setRoomList(String[] rooms) {
        //ulControl.clearList();
        /* grab pRoomList */
        roomListControl.replaceList(rooms);
    }
    
    /**
     * Return the GroupTabControl
     */
    public GroupTabControl getGroupTabControl() {
        return groupTabControl;
    }
    
    /**
     * Return the PrivTabControl
     */
    public PrivTabControl getPrivTabControl() {
        return privTabControl;
    }    
    
    /**
     * Set the username of the user in the ControlPanel.
     *
     * @param username The username to change to
     */
    public void setUsername(String username) {
        pControl.tfUserName.setText(username);
    }
    
}