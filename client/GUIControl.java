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
    
    private int connectionStatus;
    
    public static int CONNECTING = 1;
    
    public static int CONNECTED = 2;
    
    public static int DISCONNECTING = 3;
    
    public static int DISCONNECTED = 4;
    
    /** Creates a new instance of GUIControl */
    public GUIControl(UserListControl ul, ChatAreaControl ca, JButton con, JButton dis, JLabel constat, JTextArea erroroutput) {
        ulControl = ul;
        caControl = ca;
        bConnect = con;
        bDisconnect = dis;
        lConnectionStatus = constat;
        taErrorOutput = erroroutput;
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
                break;
            case 2: // Connected
                bConnect.setEnabled(false);
                bDisconnect.setEnabled(true);     
                lConnectionStatus.setText("Connected");
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
