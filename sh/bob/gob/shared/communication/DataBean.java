/*
 * DataBean.java
 *
 * Created on 2 November 2003, 12:02
 */

package sh.bob.gob.shared.communication;

import sh.bob.gob.shared.validation.*;

import java.beans.*;


/**
 *
 * @author  ken
 */
public class DataBean extends Object implements java.io.Serializable {
    
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property success. */
    private boolean success;
    
    /** Holds value of property error. */
    private String error;
    
    /** Creates new Action */
    public DataBean() {
        error = null;
        success = false;
        
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property success.
     * @return Value of property success.
     *
     */
    public boolean isSuccess() {
        return this.success;
    }
    
    /** Setter for property success.
     * @param success New value of property success.
     *
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    /** Getter for property error.
     * @return Value of property error.
     *
     */
    public String getError() {
        return this.error;
    }
    
    /** Setter for property error.
     * @param error New value of property error.
     *
     */
    public void setError(String error) throws TextInvalidException {
        TextValidation.isMessage(error);
        
        this.error = error;
    }
    
}
