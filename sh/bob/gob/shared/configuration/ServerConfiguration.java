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
    
    /** Holds value of property logging. */
    private Logging logging;
    
    /** Holds value of property network. */
    private Network network;
    
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
    
    /** Getter for property logging.
     * @return Value of property logging.
     *
     */
    public Logging getLogging() {
        return this.logging;
    }
    
    /** Setter for property logging.
     * @param logging New value of property logging.
     *
     */
    public void setLogging(Logging logging) {
        this.logging = logging;
    }
    
    /** Getter for property network.
     * @return Value of property network.
     *
     */
    public Network getNetwork() {
        return this.network;
    }
    
    /** Setter for property network.
     * @param network New value of property network.
     *
     */
    public void setNetwork(Network network) {
        this.network = network;
    }
    
}
