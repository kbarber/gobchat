/*
 * RoomListPanel.java
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
public class RoomListPanel extends javax.swing.JPanel {
    
    /** Creates new form ChatPanel */
    public RoomListPanel() {
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

        setLayout(null);

        spRooms.setBackground(new java.awt.Color(204, 204, 255));
        spRooms.setBorder(null);
        lRooms.setToolTipText("List of users");
        spRooms.setViewportView(lRooms);

        add(spRooms);
        spRooms.setBounds(0, 0, 504, 322);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList lRooms;
    private javax.swing.JScrollPane spRooms;
    // End of variables declaration//GEN-END:variables
    
}
