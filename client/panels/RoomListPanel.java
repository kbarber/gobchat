/*
 * RoomListPanel.java
 *
 * Created on 11 August 2003, 01:04
 */

package client.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import client.controllers.*;
import client.*;

/**
 * This panel is used to list the available chat rooms.
 *
 * @author  ken
 */
public class RoomListPanel extends javax.swing.JPanel {
    
    private GUIControl guiControl;
    private ClientConnectionControl ccControl;
    
    /** Creates new form ChatPanel */
    public RoomListPanel(GUIControl gui, ClientConnectionControl ccc) {
        guiControl = gui;
        ccControl = ccc;
        
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        spRooms = new javax.swing.JScrollPane();
        lRooms = new javax.swing.JList();
        tfRoom = new javax.swing.JTextField();
        bJoinRoom = new javax.swing.JButton();
        bRefreshList = new javax.swing.JButton();

        setLayout(null);

        spRooms.setBackground(new java.awt.Color(204, 204, 255));
        spRooms.setBorder(null);
        lRooms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lRooms.setToolTipText("List of rooms");
        lRooms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lRoomsMouseClicked(evt);
            }
        });

        spRooms.setViewportView(lRooms);

        add(spRooms);
        spRooms.setBounds(0, 0, 504, 298);

        tfRoom.setToolTipText("Selected room");
        tfRoom.setFocusCycleRoot(true);
        tfRoom.setAutoscrolls(false);
        add(tfRoom);
        tfRoom.setBounds(0, 300, 388, 24);

        bJoinRoom.setFont(new java.awt.Font("Dialog", 0, 12));
        bJoinRoom.setText("Join");
        bJoinRoom.setToolTipText("Click here to join the chosen room");
        bJoinRoom.setActionCommand("jButton1");
        bJoinRoom.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bJoinRoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bJoinRoomMouseClicked(evt);
            }
        });

        add(bJoinRoom);
        bJoinRoom.setBounds(390, 300, 42, 23);

        bRefreshList.setFont(new java.awt.Font("Dialog", 0, 12));
        bRefreshList.setText("Refresh list");
        bRefreshList.setToolTipText("Click here to refresh the room list");
        bRefreshList.setActionCommand("jButton1");
        bRefreshList.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        bRefreshList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bRefreshListMouseClicked(evt);
            }
        });

        add(bRefreshList);
        bRefreshList.setBounds(434, 300, 70, 23);

    }//GEN-END:initComponents

    private void bJoinRoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bJoinRoomMouseClicked
        // Add your handling code here:
        
        /* obtain the currently selected room from tfRoom.getText */
        /* Send a command to the server to join the room */
        ccControl.sendCommand("join:" + tfRoom.getText());  
        ccControl.sendCommand("roomlist:*");
    }//GEN-LAST:event_bJoinRoomMouseClicked

    private void lRoomsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lRoomsMouseClicked
        // Add your handling code here:
        
        /* get the selected value */
        /* set tfRoom to this value */
        tfRoom.setText((String)lRooms.getSelectedValue());
        
    }//GEN-LAST:event_lRoomsMouseClicked

    private void bRefreshListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bRefreshListMouseClicked
        // Add your handling code here:
        ccControl.sendCommand("roomlist:*");
    }//GEN-LAST:event_bRefreshListMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bJoinRoom;
    public javax.swing.JButton bRefreshList;
    public javax.swing.JList lRooms;
    private javax.swing.JScrollPane spRooms;
    public javax.swing.JTextField tfRoom;
    // End of variables declaration//GEN-END:variables
    
}
