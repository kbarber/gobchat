/*
 * GUIControl.java
 *
 * Created on 11 January 2003, 22:13
 */

package client;

import javax.swing.*;
import java.util.*;

import client.components.*;
import client.panels.*;
import client.*;

/**
 *
 * @author  Ken Barber
 */
public class GUIControl {
    
    private UserListControl ulControl;
    private MsgAreaControl maControl;
    private JButton bConnect;
    private JButton bDisconnect;
    private JLabel lConnectionStatus;
    private JTextArea taErrorOutput;
    private JTabbedPane tbMain;
    private JTextField tfUserName;
    private JTextField tfSendPrep;
    private JPanel pLobby;
    
    private int connectionStatus;
    
    public static int CONNECTING = 1;
    
    public static int CONNECTED = 2;
    
    public static int DISCONNECTING = 3;
    
    public static int DISCONNECTED = 4;
    
    /** Creates a new instance of GUIControl */
    public GUIControl(UserListControl ul, MsgAreaControl ma, JButton con, JButton dis, JLabel constat, JTextArea erroroutput, JTabbedPane tabbedpane, JTextField username, JTextField sendprep, JPanel lobby) {
        ulControl = ul;
        maControl = ma;
        bConnect = con;
        bDisconnect = dis;
        lConnectionStatus = constat;
        taErrorOutput = erroroutput;
        tbMain = tabbedpane;
        tfUserName = username;
        tfSendPrep = sendprep;
        pLobby = lobby;
    }
    
    public void setConnected(int status) {
        setConnected(status, "No reason");
    }
    
    public void setConnected(int status, String reason) {
        
        connectionStatus = status;
        
        switch(status) {
            case 1: // Connecting
                bConnect.setEnabled(false);
                bDisconnect.setEnabled(true);                
                lConnectionStatus.setText("Connecting ...");
                
                /* And disable changing the username field */
                tfUserName.setEditable(false);
                break;
            case 2: // Connected
                bConnect.setEnabled(false);
                bDisconnect.setEnabled(true);     
                lConnectionStatus.setText("Connected");
                
                /* Add the lobby tab to the tabbed pain */
                tbMain.addTab("Lobby", pLobby);
                
                /* Now open the lobby tab */
                tbMain.setSelectedIndex(2);
                
                /* And focus on the sendprep area */
                tfSendPrep.requestFocusInWindow();
                break;
            case 3: // Disconnecting   
                bConnect.setEnabled(true);
                bDisconnect.setEnabled(false);
                lConnectionStatus.setText("Disconnecting ...");
                break;
            case 4: // Disconnected
                bConnect.setEnabled(true);
                bDisconnect.setEnabled(false);
                
                /* Set a reason for disconnection */
                lConnectionStatus.setText("Disconnected: " + reason);
                
                /* And enable the username field */
                tfUserName.setEditable(true);
                
                /* And now request focus if this tab is open */
                tfUserName.requestFocusInWindow();
                
                /* Now remove the lobby */
                tbMain.removeTabAt(2);
                break;
        }
    }
    
    public int getConnected() {
        return connectionStatus;
    }
    
    public void printError(String output) {
        taErrorOutput.append(new Date().toString() + ": " + output + "\n");
    }
    
    public void statusMessage(String output) {
        maControl.statusMessage(output);
    }
    
    public void userMessage(String un, String output) {
        maControl.userMessage(un, output);
    }
    
    public void clearTextArea() {
        maControl.clearTextArea();
    }
    
    public void addUser(String un) {
        ulControl.addUser(un);
    }
    
    public void deleteUser(String un) {
        ulControl.deleteUser(un);
    }
    
    public void clearList() {
        ulControl.clearList();
    }
    
}