/*
 * ServerMessage.java
 *
 * Created on 1 November 2003, 21:56
 */

package sh.bob.gob.shared.communication;

import sh.bob.gob.shared.validation.*;

import java.beans.*;

/**
 *
 * @author  ken
 */
public class ServerMessage extends Object implements java.io.Serializable {
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property message. */
    private String message;
    
    /** Creates new ServerMessage */
    public ServerMessage() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    /** Creates new ServerMessage */
    public ServerMessage(String message) throws TextInvalidException {
        propertySupport = new PropertyChangeSupport( this );
        
        setMessage(message);
    }

    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property message.
     * @return Value of property message.
     *
     */
    public String getMessage() {
        return this.message;
    }
    
    /** Setter for property message.
     * @param message New value of property message.
     *
     */
    public void setMessage(String message) throws TextInvalidException {
        TextValidation.isMessage(message);
        this.message = message;
    }
    
}
