/*
 * Logging.java
 *
 * Created on 1 November 2003, 19:20
 */

package sh.bob.gob.shared.configuration;

import java.beans.*;

/**
 *
 * @author  Ken Barber
 */
public class Logging extends Object implements java.io.Serializable {
    
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property logFile. */
    private String logFile;
    
    /** Holds value of property logLevel. */
    private String logLevel;
    
    /** Creates new Logging */
    public Logging() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property logFile.
     * @return Value of property logFile.
     *
     */
    public String getLogFile() {
        return this.logFile;
    }
    
    /** Setter for property logFile.
     * @param logFile New value of property logFile.
     *
     */
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
    
    /** Getter for property logLevel.
     * @return Value of property logLevel.
     *
     */
    public String getLogLevel() {
        return this.logLevel;
    }
    
    /** Setter for property logLevel.
     * @param logLevel New value of property logLevel.
     *
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
}
