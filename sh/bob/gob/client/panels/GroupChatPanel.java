/*
 * GroupChatPanel.java
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

import sh.bob.gob.client.controllers.*;
import sh.bob.gob.client.*;
import sh.bob.gob.client.components.*;
import sh.bob.gob.shared.communication.*;
import sh.bob.gob.shared.validation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This panel is the group chat panel, for areas of chat that have multiple people
 * involved.
 *
 * @author  ken
 */
public class GroupChatPanel extends javax.swing.JPanel {

    private GUIControl guiControl;
    private ClientConnectionControl ccControl;
    private String groupName;
    private ListControl listControl;
    private MsgAreaControl msgAreaControl;
    
    /** 
     * Creates new form ChatPanel 
     *
     * @param gui The GUIControl interface
     * @param ccc The ClientConnectionControl interface
     * @param name The name of the panel
     */
    public GroupChatPanel(GUIControl gui, ClientConnectionControl ccc, String name) {
        guiControl = gui;
        ccControl = ccc;
        groupName = name;
        
        initComponents();
        
        listControl = new ListControl(lUsers);
        msgAreaControl = new MsgAreaControl(taMsgHistory);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        spMsgUsers = new javax.swing.JSplitPane();
        spMsgHistory = new javax.swing.JScrollPane();
        taMsgHistory = new javax.swing.JTextArea();
        spUsers = new javax.swing.JScrollPane();
        lUsers = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        lPriv = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        bClose = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        tfSendPrep = new javax.swing.JTextField();
        bSendText = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        spMsgUsers.setBackground(new java.awt.Color(204, 204, 255));
        spMsgUsers.setBorder(null);
        spMsgUsers.setDividerLocation(400);
        spMsgUsers.setDividerSize(1);
        spMsgUsers.setResizeWeight(1.0);
        spMsgUsers.setContinuousLayout(true);
        spMsgUsers.setDoubleBuffered(true);
        spMsgHistory.setBackground(new java.awt.Color(204, 204, 255));
        taMsgHistory.setBackground(new java.awt.Color(250, 250, 255));
        taMsgHistory.setEditable(false);
        taMsgHistory.setLineWrap(true);
        taMsgHistory.setWrapStyleWord(true);
        taMsgHistory.setBorder(null);
        taMsgHistory.setDoubleBuffered(true);
        taMsgHistory.setDragEnabled(true);
        spMsgHistory.setViewportView(taMsgHistory);

        spMsgUsers.setLeftComponent(spMsgHistory);

        spUsers.setBackground(new java.awt.Color(204, 204, 255));
        spUsers.setMaximumSize(new java.awt.Dimension(40, 32767));
        lUsers.setBackground(new java.awt.Color(250, 250, 250));
        lUsers.setToolTipText("List of users");
        spUsers.setViewportView(lUsers);

        spMsgUsers.setRightComponent(spUsers);

        add(spMsgUsers, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        lPriv.setText("Priv");
        lPriv.setToolTipText("Click here for a private chat");
        lPriv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lPrivMouseClicked(evt);
            }
        });

        jPanel2.add(lPriv, java.awt.BorderLayout.EAST);

        bClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bCloseMouseClicked(evt);
            }
        });

        jPanel3.add(bClose);

        jPanel2.add(jPanel3, java.awt.BorderLayout.WEST);

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        tfSendPrep.setToolTipText("Type your message here");
        tfSendPrep.setFocusCycleRoot(true);
        tfSendPrep.setAutoscrolls(false);
        tfSendPrep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfSendPrepKeyPressed(evt);
            }
        });

        jPanel1.add(tfSendPrep, java.awt.BorderLayout.CENTER);

        bSendText.setFont(new java.awt.Font("SansSerif", 1, 14));
        bSendText.setText("SEND");
        bSendText.setToolTipText("Click here to send");
        bSendText.setActionCommand("jButton1");
        bSendText.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bSendText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bSendTextMouseClicked(evt);
            }
        });

        jPanel1.add(bSendText, java.awt.BorderLayout.EAST);

        jPanel4.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel4.add(jPanel6, java.awt.BorderLayout.SOUTH);

        add(jPanel4, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    private void lPrivMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lPrivMouseClicked
        /* take the selected balue from the user list */
        String user = (String)lUsers.getSelectedValue();
        
        /* Lets just make sure that the user selected isn't the current user */
        /* Well, you could chat to yourself. But I find I never have much to
         * say */
        if(user.equals(ccControl.getConnectionInfo().getUsername())) {
            /* Bail */
            return;
        } else { 
            /* Open a new chat panel */
            guiControl.getPrivTabControl().addUser(user);
        }
    }//GEN-LAST:event_lPrivMouseClicked

    private void bCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCloseMouseClicked
        // Add your handling code here:
        RoomPart rp = new RoomPart();
        try {
            rp.setRoomName(groupName);
            rp.setUserName(ccControl.getConnectionInfo().getUsername());
        } catch (TextInvalidException ex) {
            guiControl.statusMessage("Invalid group name or username to close: " + ex);
        }
        ccControl.sendData(rp);
        
        RoomList rl = new RoomList();
        try {
            rl.setFilter("*");
        } catch (TextInvalidException ex) {
        }
        ccControl.sendData(rl);
    }//GEN-LAST:event_bCloseMouseClicked

    private void tfSendPrepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSendPrepKeyPressed
        // Add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
        
    }//GEN-LAST:event_tfSendPrepKeyPressed

    private void bSendTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSendTextMouseClicked
        // Add your handling code here:
        sendMessage();
        
                
    }//GEN-LAST:event_bSendTextMouseClicked
    
    private void sendMessage() {
        RoomSend rs = new RoomSend();
        try {
            rs.setRoomName(groupName);
        } catch (TextInvalidException ex) {
            guiControl.statusMessage("Invalid group name: " + ex);
            return;
        }
        
        try {
            rs.setMessage(tfSendPrep.getText());
        } catch (TextInvalidException ex) {
            guiControl.statusMessage("Invalid message: " + ex);
            return;
        }
        ccControl.sendData(rs);
        tfSendPrep.setText("");
        tfSendPrep.requestFocus();
    }
    
    /**
     * Resets the user list.
     *
     * @param users Array of users
     */
    public void resetUserList(String users[]) {
        listControl.replaceList(users);
    }
    
    public void addUser(String name) {
        listControl.addUser(name);
    }
    
    public void deleteUser(String name) {
        listControl.deleteUser(name);
    }
    
    public boolean checkUser(String name) {
        return listControl.checkUser(name);
    }
    
    public void writeUserMessage(String name, String message) {
        msgAreaControl.userMessage(name, message);
    }
    
    public void writeStatusMessage(String message) {
        msgAreaControl.statusMessage(message);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClose;
    public javax.swing.JButton bSendText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lPriv;
    public javax.swing.JList lUsers;
    private javax.swing.JScrollPane spMsgHistory;
    private javax.swing.JSplitPane spMsgUsers;
    private javax.swing.JScrollPane spUsers;
    public javax.swing.JTextArea taMsgHistory;
    public javax.swing.JTextField tfSendPrep;
    // End of variables declaration//GEN-END:variables
    
}
