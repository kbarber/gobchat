/*
 * ControlPanel.java
 *
 * Created on 11 August 2003, 01:04
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

package sh.bob.gob.client.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

import sh.bob.gob.client.controllers.*;
import sh.bob.gob.client.*;
import sh.bob.gob.shared.communication.*;


/**
 * This panel holds widgets which allow you to connect, disconnect, change your username etc.
 *
 * @author  ken
 */
public class ControlPanel extends javax.swing.JPanel {
    
    private GUIControl guiControl;
    private ClientConnectionControl conControl;
    private String hostname;
    
    /** 
     * Creates new form ChatPanel 
     *
     * @param gui The GUIControl interface to utilise
     * @param ccc The ClientConnectionControl interface
     * @param host The hostname of the gob server
     */
    public ControlPanel(GUIControl gui, ClientConnectionControl ccc, String host) {
        guiControl = gui;
        conControl = ccc;
        hostname = host;
        
        if(hostname == null) {
            hostname = "localhost";
        }
        
        if(hostname.length() > 0) {
        } else {
            hostname = "localhost";
        }
        
        initComponents();

        /* Set the server */
        tfGobServer.setText(hostname);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel4 = new javax.swing.JPanel();
        pLogon = new javax.swing.JPanel();
        bConnect = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        tfGobServer = new javax.swing.JTextField();
        tfUserName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        lUserName = new javax.swing.JLabel();
        lHost = new javax.swing.JLabel();
        lGob = new javax.swing.JLabel();
        pRename = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        bRename = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        tfNewUserName = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        lNewUserName = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        spSystemLog = new javax.swing.JScrollPane();
        taSystemLog = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        bDisconnect = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        pLogon.setLayout(null);

        bConnect.setFont(new java.awt.Font("SansSerif", 1, 14));
        bConnect.setText("CONNECT");
        bConnect.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bConnectActionPerformed(evt);
            }
        });
        bConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bConnectMouseClicked(evt);
            }
        });

        pLogon.add(bConnect);
        bConnect.setBounds(210, 230, 90, 30);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        tfGobServer.setEditable(false);
        tfGobServer.setFont(new java.awt.Font("SansSerif", 0, 12));
        tfGobServer.setText("localhost");
        jPanel3.add(tfGobServer, java.awt.BorderLayout.SOUTH);

        tfUserName.setFont(new java.awt.Font("SansSerif", 0, 12));
        tfUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfUserNameKeyPressed(evt);
            }
        });

        jPanel3.add(tfUserName, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.add(jPanel17, java.awt.BorderLayout.EAST);

        jPanel18.setLayout(new java.awt.BorderLayout());

        lUserName.setFont(new java.awt.Font("SansSerif", 1, 12));
        lUserName.setText("USERNAME");
        jPanel18.add(lUserName, java.awt.BorderLayout.NORTH);

        lHost.setFont(new java.awt.Font("SansSerif", 1, 12));
        lHost.setText("HOST");
        jPanel18.add(lHost, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);

        pLogon.add(jPanel1);
        jPanel1.setBounds(150, 120, 198, 51);

        lGob.setFont(new java.awt.Font("SansSerif", 1, 48));
        lGob.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lGob.setText("GOB");
        pLogon.add(lGob);
        lGob.setBounds(200, 20, 109, 62);

        add(pLogon, "Logon");

        pRename.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(new javax.swing.border.TitledBorder("RENAME"));
        jPanel9.setLayout(new java.awt.BorderLayout());

        bRename.setFont(new java.awt.Font("SansSerif", 1, 12));
        bRename.setText("RENAME");
        bRename.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRenameActionPerformed(evt);
            }
        });
        bRename.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bRenameMouseClicked(evt);
            }
        });

        jPanel9.add(bRename, java.awt.BorderLayout.NORTH);

        jPanel6.add(jPanel9, java.awt.BorderLayout.EAST);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());

        tfNewUserName.setFont(new java.awt.Font("SansSerif", 0, 12));
        tfNewUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfNewUserNameKeyPressed(evt);
            }
        });

        jPanel11.add(tfNewUserName, java.awt.BorderLayout.NORTH);

        jPanel10.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel15.add(jPanel16, java.awt.BorderLayout.EAST);

        lNewUserName.setFont(new java.awt.Font("SansSerif", 1, 12));
        lNewUserName.setText("NEW USERNAME");
        jPanel15.add(lNewUserName, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel15, java.awt.BorderLayout.NORTH);

        jPanel10.add(jPanel12, java.awt.BorderLayout.WEST);

        jPanel10.add(jPanel13, java.awt.BorderLayout.EAST);

        jPanel6.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel14, java.awt.BorderLayout.SOUTH);

        pRename.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.X_AXIS));

        jPanel8.setBorder(new javax.swing.border.TitledBorder("SYSTEM LOG"));
        taSystemLog.setEditable(false);
        taSystemLog.setFont(new java.awt.Font("SansSerif", 0, 12));
        taSystemLog.setLineWrap(true);
        spSystemLog.setViewportView(taSystemLog);

        jPanel8.add(spSystemLog);

        jPanel7.add(jPanel8, java.awt.BorderLayout.CENTER);

        bDisconnect.setFont(new java.awt.Font("SansSerif", 1, 12));
        bDisconnect.setText("LOGOFF");
        bDisconnect.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDisconnectActionPerformed(evt);
            }
        });
        bDisconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bDisconnectMouseClicked(evt);
            }
        });

        jPanel19.add(bDisconnect);

        jPanel7.add(jPanel19, java.awt.BorderLayout.SOUTH);

        pRename.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(pRename, "Rename");

    }//GEN-END:initComponents

    private void bDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDisconnectActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_bDisconnectActionPerformed

    private void bRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRenameActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_bRenameActionPerformed

    private void bConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bConnectActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_bConnectActionPerformed

    private void tfNewUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNewUserNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            renameUser();
        }
    }//GEN-LAST:event_tfNewUserNameKeyPressed

    private void bRenameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bRenameMouseClicked
        renameUser();
    }//GEN-LAST:event_bRenameMouseClicked

    private void bConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bConnectMouseClicked
        serverConnect();
    }//GEN-LAST:event_bConnectMouseClicked

    private void bDisconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bDisconnectMouseClicked
        serverDisconnect("Hit disconnect button");
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
        
        /* Spawn a connection thread and connect */
        conControl.serverConnect(tfUserName.getText(), tfGobServer.getText());
      
    }
    
    /**
     * Code to interrupt the connection thread.
     *
     * @param message Disconnection message to pass to server
     */
    private void serverDisconnect(String message) {
        conControl.serverDisconnect(message);
    }
    
    /** 
     * Rename user
     *
     * @param name New username
     */
    private void renameUser() {
        //conControl.sendCommand("rename:" + tfNewUserName.getText());
        NameChange nc = null;
        try {
            nc = new NameChange (
                conControl.getConnectionInfo().getUsername(), 
                tfNewUserName.getText()
                );
        } catch (Exception ex) {
            Logger.getLogger("sh.bob.gob.client").severe("Unable to get current username: " + ex);
            return;
        }

        
        conControl.sendData (nc);
        tfNewUserName.setText("");
    }
    
    /**
     * The method first executed after the object has been initiated.
     */
    public void init() {
        /* Obtain the hostname and set it in the GUI */
        
        if(hostname.length() > 0) {
            /* Set the host parameter as the host to connect to */
            tfGobServer.setText(hostname);
        } else {
            /* Else just default to localhost */
            tfGobServer.setText("localhost");
        }
        
        
        /* Focus on the username textfield */
        tfUserName.requestFocusInWindow();
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bConnect;
    private javax.swing.JButton bDisconnect;
    private javax.swing.JButton bRename;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lGob;
    private javax.swing.JLabel lHost;
    private javax.swing.JLabel lNewUserName;
    private javax.swing.JLabel lUserName;
    private javax.swing.JPanel pLogon;
    private javax.swing.JPanel pRename;
    private javax.swing.JScrollPane spSystemLog;
    public javax.swing.JTextArea taSystemLog;
    private javax.swing.JTextField tfGobServer;
    private javax.swing.JTextField tfNewUserName;
    public javax.swing.JTextField tfUserName;
    // End of variables declaration//GEN-END:variables

}
