/*
 * GUIControl.java
 *
 * Created on 11 January 2003, 22:13
 */

package client;

import javax.swing.*;
import java.util.*;

/**
 *
 * @author  Ken Barber
 */
public class GUIControl {
    
    private UserListControl ulControl;
    private ChatAreaControl caControl;
    private JButton bConnect;
    private JButton bDisconnect;
    private JLabel lConnectionStatus;
    private JTextArea taErrorOutput;
    private JTabbedPane tbMain;
    private JTextField tfUserName;
    private JTextField tfSendPrep;
    
    private int connectionStatus;
    
    public static int CONNECTING = 1;
    
    public static int CONNECTED = 2;
    
    public static int DISCONNECTING = 3;
    
    public static int DISCONNECTED = 4;
    
    /** Creates a new instance of GUIControl */
    public GUIControl(UserListControl ul, ChatAreaControl ca, JButton con, JButton dis, JLabel constat, JTextArea erroroutput, JTabbedPane tabbedpane, JTextField username, JTextField sendprep) {
        ulControl = ul;
        caControl = ca;
        bConnect = con;
        bDisconnect = dis;
        lConnectionStatus = constat;
        taErrorOutput = erroroutput;
        tbMain = tabbedpane;
        tfUserName = username;
        tfSendPrep = sendprep;
    }
    
    public void setConnected(int status) {
        setConnected(status, "");
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
                
                /* Now open the lobby tab */
                tbMain.setSelectedIndex(1);
                
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
                lConnectionStatus.setText("Disconnected: " + reason);
                
                /* And enable the username field */
                tfUserName.setEditable(true);
                
                /* And now request focus if this tab is open */
                tfUserName.requestFocusInWindow();
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
        caControl.statusMessage(output);
    }
    
    public void userMessage(String un, String output) {
        caControl.userMessage(un, output);
    }
    
    public void clearTextArea() {
        caControl.clearTextArea();
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
