/*
 * ControlPanel.java
 *
 * Created on 11 August 2003, 01:04
 */

package client.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author  ken
 */
public class ControlPanel extends javax.swing.JPanel {
    
    /** Creates new form ChatPanel */
    public ControlPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        pConnection = new javax.swing.JPanel();
        lUserName = new javax.swing.JLabel();
        lHost = new javax.swing.JLabel();
        tfUserName = new javax.swing.JTextField();
        tfGobServer = new javax.swing.JTextField();
        bDisconnect = new javax.swing.JButton();
        bConnect = new javax.swing.JButton();
        lConnectionStatus = new javax.swing.JLabel();
        pUser = new javax.swing.JPanel();
        lNewUserName = new javax.swing.JLabel();
        tfNewUserName = new javax.swing.JTextField();
        bRename = new javax.swing.JButton();

        setLayout(null);

        setBackground(new java.awt.Color(204, 204, 255));
        pConnection.setLayout(null);

        pConnection.setBackground(new java.awt.Color(204, 204, 255));
        pConnection.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(null, new java.awt.Color(153, 153, 255)), "Connection"));
        lUserName.setFont(new java.awt.Font("Dialog", 0, 12));
        lUserName.setText("Username");
        pConnection.add(lUserName);
        lUserName.setBounds(10, 20, 70, 20);

        lHost.setFont(new java.awt.Font("Dialog", 0, 12));
        lHost.setText("Host");
        pConnection.add(lHost);
        lHost.setBounds(10, 50, 60, 15);

        tfUserName.setToolTipText("Type your username here");
        tfUserName.setDisabledTextColor(new java.awt.Color(204, 204, 255));
        tfUserName.setFocusCycleRoot(true);
        tfUserName.setAutoscrolls(false);
        tfUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfUserNameKeyPressed(evt);
            }
        });

        pConnection.add(tfUserName);
        tfUserName.setBounds(80, 20, 220, 20);

        tfGobServer.setBackground(new java.awt.Color(204, 204, 255));
        tfGobServer.setEditable(false);
        tfGobServer.setToolTipText("Host to connect to");
        tfGobServer.setBorder(null);
        pConnection.add(tfGobServer);
        tfGobServer.setBounds(80, 50, 220, 15);

        bDisconnect.setBackground(new java.awt.Color(204, 204, 255));
        bDisconnect.setFont(new java.awt.Font("Dialog", 0, 12));
        bDisconnect.setText("Disconnect");
        bDisconnect.setToolTipText("Click to disconnect");
        bDisconnect.setActionCommand("jButton1");
        bDisconnect.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bDisconnect.setEnabled(false);
        bDisconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bDisconnectMouseClicked(evt);
            }
        });

        pConnection.add(bDisconnect);
        bDisconnect.setBounds(414, 50, 80, 20);

        bConnect.setBackground(new java.awt.Color(204, 204, 255));
        bConnect.setFont(new java.awt.Font("Dialog", 0, 12));
        bConnect.setText("Connect");
        bConnect.setToolTipText("Click to connect");
        bConnect.setActionCommand("jButton1");
        bConnect.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bConnectMouseClicked(evt);
            }
        });

        pConnection.add(bConnect);
        bConnect.setBounds(414, 20, 80, 20);

        lConnectionStatus.setFont(new java.awt.Font("Dialog", 0, 12));
        lConnectionStatus.setText("Disconnected");
        lConnectionStatus.setToolTipText("Status");
        lConnectionStatus.setBorder(new javax.swing.border.EtchedBorder(null, new java.awt.Color(153, 153, 255)));
        pConnection.add(lConnectionStatus);
        lConnectionStatus.setBounds(10, 80, 484, 20);

        add(pConnection);
        pConnection.setBounds(0, 0, 505, 112);

        pUser.setLayout(null);

        pUser.setBackground(new java.awt.Color(204, 204, 255));
        pUser.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(null, new java.awt.Color(153, 153, 255)), "User"));
        lNewUserName.setFont(new java.awt.Font("Dialog", 0, 12));
        lNewUserName.setText("New Username");
        pUser.add(lNewUserName);
        lNewUserName.setBounds(10, 20, 100, 20);

        tfNewUserName.setEditable(false);
        tfNewUserName.setToolTipText("Type your username here");
        tfNewUserName.setDisabledTextColor(new java.awt.Color(204, 204, 255));
        tfNewUserName.setFocusCycleRoot(true);
        tfNewUserName.setAutoscrolls(false);
        tfNewUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfNewUserNameKeyPressed(evt);
            }
        });

        pUser.add(tfNewUserName);
        tfNewUserName.setBounds(110, 20, 220, 20);

        bRename.setBackground(new java.awt.Color(204, 204, 255));
        bRename.setFont(new java.awt.Font("Dialog", 0, 12));
        bRename.setText("Rename");
        bRename.setToolTipText("Click to connect");
        bRename.setActionCommand("jButton1");
        bRename.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bRename.setEnabled(false);
        bRename.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bRenameMouseClicked(evt);
            }
        });

        pUser.add(bRename);
        bRename.setBounds(414, 20, 80, 20);

        add(pUser);
        pUser.setBounds(0, 116, 505, 50);

    }//GEN-END:initComponents

    private void tfNewUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNewUserNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {

        }
    }//GEN-LAST:event_tfNewUserNameKeyPressed

    private void bRenameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bRenameMouseClicked
        
    }//GEN-LAST:event_bRenameMouseClicked

    private void bConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bConnectMouseClicked
        serverConnect();
    }//GEN-LAST:event_bConnectMouseClicked

    private void bDisconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bDisconnectMouseClicked
        serverDisconnect();
    }//GEN-LAST:event_bDisconnectMouseClicked

    private void tfUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfUserNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            serverConnect();
        }
    }//GEN-LAST:event_tfUserNameKeyPressed
    
    /**
     * Code to spawn a new connect thread, and connect to the server.
     */
    private void serverConnect() {
        /* Obtain username and hostname from GUI */

    }
    
    /**
     * Code to interrupt the connection thread.
     */
    private void serverDisconnect() {
    }
    
    /**
     * The method first executed after the object has been initiated.
     */
    public void init() {
        /* Obtain the "host" parameter from the web page hosting the
         * applet */
        
        //if(getParameter("host").length() > 0) {
        //    /* Set the host parameter as the host to connect to */
        //    tfGobServer.setText(getParameter("host"));
        //} else {
        //    /* Else just default to localhost */
        //    tfGobServer.setText("localhost");
        //}
        
        
        /* Focus on the username textfield */
        tfUserName.requestFocusInWindow();
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bConnect;
    public javax.swing.JButton bDisconnect;
    public javax.swing.JButton bRename;
    public javax.swing.JLabel lConnectionStatus;
    private javax.swing.JLabel lHost;
    private javax.swing.JLabel lNewUserName;
    private javax.swing.JLabel lUserName;
    private javax.swing.JPanel pConnection;
    private javax.swing.JPanel pUser;
    public javax.swing.JTextField tfGobServer;
    public javax.swing.JTextField tfNewUserName;
    public javax.swing.JTextField tfUserName;
    // End of variables declaration//GEN-END:variables
    
}
