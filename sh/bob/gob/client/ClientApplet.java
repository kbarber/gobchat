/*
 * ClientApplet.java
 *
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

package sh.bob.gob.client;

import sh.bob.gob.client.controllers.ClientConnectionControl;
import sh.bob.gob.client.controllers.GUIControl;
import sh.bob.gob.client.network.ConnectionInfo;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.logging.*;
import java.awt.Dimension;


/**
 * 
 * <p>This is the main JApplet class. From this all other classes are instantiated.
 *
 * <p>GUIControl is responsible for display control. ClientConnectionControl is
 * a network control sub-process.
 *
 * <p>ClientApplet creates a JTabbedPain object, and gives control of this to
 * GUIControl. From here, ClientConnectionControl and GUIControl will work 
 * independently, updating the GUI by itself.
 *
 * <p>ConnectionInfo is a data object, used to hold shared data.
 *
 * @author  Ken Barber
 */
public class ClientApplet extends JApplet {
    
    private ConnectionInfo conInfo;
    private ClientConnectionControl conControl;
    private GUIControl guiControl;
    private String Hostname;
    
    /** 
     * Creates new ClientApp applet. The primary part of the Gob client.
     */
    public ClientApplet() {
        
        /* Set the look and feel of the GUI */
        try {
            /* Update the look and feel with the local look and feel */
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            /* Update any components */
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) { /* There are that many exceptions that may occur, I have defaulted */
            System.out.println("Inability to set look and feel: " + e);
        } 
        
        /* Build all NetBeans generated components */
        initComponents();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        tbMain = new javax.swing.JTabbedPane();

        getContentPane().setLayout(new java.awt.CardLayout());

        setBackground(new java.awt.Color(204, 204, 255));
        setName("mainapplet");
        tbMain.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tbMain.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tbMain.setFont(new java.awt.Font("SansSerif", 1, 12));
        tbMain.setOpaque(true);
        getContentPane().add(tbMain, "card2");

    }//GEN-END:initComponents

    /**
     * <p>The method first executed after the object has been initiated.
     *
     * <p>It intantiates the main three objects, GUIControl and ClientConnectionControl
     * references are passed to each other for co-operative control.
     */
    public void init() {
        
        /* Initialise the connection info object */
        conInfo = new ConnectionInfo();
        
        guiControl = new GUIControl(tbMain, this.getParameter("host"));
        
        /* Create a new concontrol object */
        conControl = new ClientConnectionControl(guiControl, conInfo);
        
        guiControl.setClientConnectionControl(conControl);
        guiControl.displayGUI();
        
//        guiControl.statusMessage(this.getParameter("host"));
    }
    
    /**
     * <p>This method is called by the browser when attempting to destroy the
     * applet. 
     *
     * <p>This destruction is usually caused by the browser window holding the
     * applet being close.
     *
     * <p>If the applet is destroyed in this manner, disconnect from the server.
     */
    public void destroy() {
        conControl.serverDisconnect("Applet closed");
    }
    
    public void changeSize(int width, int height) {


        /* Redraw screen */
        guiControl.statusMessage("Recieved resize request - now X: " + width + " and Y: " + height);
        getRootPane().setSize(width,height);
        getRootPane().validate();
        
        Dimension dim = getRootPane().getSize();
        guiControl.statusMessage("RootPane now X: " + dim.width + " Y: " + dim.height);
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tbMain;
    // End of variables declaration//GEN-END:variables
    
}