/*
 * ServerConfiguration.java
 *
 * Created on 29 October 2003, 21:03
 */

package sh.bob.gob.shared.configuration;

import java.beans.*;

/**
 *
 * @author  ken
 */
public class ServerConfiguration extends Object implements java.io.Serializable {
        
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property TCPPort. */
    private short TCPPort;
    
    /** Holds value of property version. */
    private String version;
    
    /** Creates new ServerConfiguration */
    public ServerConfiguration() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property TCPPort.
     * @return Value of property TCPPort.
     *
     */
    public short getTCPPort() {
        return this.TCPPort;
    }
    
    /** Setter for property TCPPort.
     * @param TCPPort New value of property TCPPort.
     *
     */
    public void setTCPPort(short TCPPort) {
        this.TCPPort = TCPPort;
    }
    
    /** Getter for property version.
     * @return Value of property version.
     *
     */
    public String getVersion() {
        return this.version;
    }
    
    /** Setter for property version.
     * @param version New value of property version.
     *
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
}
