/*
 * StatusPanel.java
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
public class StatusPanel extends javax.swing.JPanel {
    
    /** Creates new form ChatPanel */
    public StatusPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        spErrorOutput = new javax.swing.JScrollPane();
        taErrorOutput = new javax.swing.JTextArea();

        setLayout(null);

        spErrorOutput.setBackground(new java.awt.Color(204, 204, 255));
        spErrorOutput.setBorder(null);
        taErrorOutput.setEditable(false);
        taErrorOutput.setLineWrap(true);
        taErrorOutput.setBorder(null);
        taErrorOutput.setDoubleBuffered(true);
        spErrorOutput.setViewportView(taErrorOutput);

        add(spErrorOutput);
        spErrorOutput.setBounds(0, 0, 510, 324);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane spErrorOutput;
    public javax.swing.JTextArea taErrorOutput;
    // End of variables declaration//GEN-END:variables
    
}
