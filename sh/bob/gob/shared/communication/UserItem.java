/*
 * UserItem.java
 *
 * Created on 1 November 2003, 21:56
 */

package sh.bob.gob.shared.communication;

import sh.bob.gob.shared.validation.*;

import java.beans.*;

/**
 *
 * @author  Ken Barber
 */
public class UserItem extends Object implements java.io.Serializable {
  
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property userName. */
    private String userName;
    
    /** Creates new UserItem */
    public UserItem() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property userName.
     * @return Value of property userName.
     *
     */
    public String getUserName() {
        return this.userName;
    }
    
    /** Setter for property userName.
     * @param userName New value of property userName.
     *
     */
    public void setUserName(String userName) throws TextInvalidException {
        TextValidation.isUserName(userName);
        this.userName = userName;
    }
    
}
