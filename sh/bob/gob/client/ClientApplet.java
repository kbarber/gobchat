/*
 * ClientApplet.java
 *
 */

package sh.bob.gob.client;

import sh.bob.gob.client.controllers.ClientConnectionControl;
import sh.bob.gob.client.controllers.GUIControl;
import sh.bob.gob.client.network.ConnectionInfo;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.logging.*;


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

        getContentPane().setLayout(null);

        setBackground(new java.awt.Color(204, 204, 255));
        setName("mainapplet");
        tbMain.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tbMain.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tbMain.setFont(new java.awt.Font("Dialog", 0, 12));
        tbMain.setOpaque(true);
        getContentPane().add(tbMain);
        tbMain.setBounds(0, 0, 510, 350);

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
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tbMain;
    // End of variables declaration//GEN-END:variables
    
}