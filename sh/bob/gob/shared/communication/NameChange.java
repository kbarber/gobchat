/*
 * NameChange.java
 *
 * Created on 1 November 2003, 21:54
 */

package sh.bob.gob.shared.communication;

import sh.bob.gob.shared.validation.*;

import java.beans.*;

/**
 *
 * @author  ken
 */
public class NameChange extends DataBean implements java.io.Serializable {
    
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property userNameOld. */
    private String userNameOld;
    
    /** Holds value of property userNameNew. */
    private String userNameNew;
    
    /** Creates new NameChange */
    public NameChange() {
    }
    
    public NameChange(String unold, String unnew) {
        
        super();
        
        userNameOld = unold;
        userNameNew = unnew;
        
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property userNameOld.
     * @return Value of property userNameOld.
     *
     */
    public String getUserNameOld() {
        return this.userNameOld;
    }
    
    /** Setter for property userNameOld.
     * @param userNameOld New value of property userNameOld.
     *
     */
    public void setUserNameOld(String userNameOld) throws TextInvalidException {
        TextValidation.isUserName(userNameOld);
        this.userNameOld = userNameOld;
    }
    
    /** Getter for property userNameNew.
     * @return Value of property userNameNew.
     *
     */
    public String getUserNameNew() {
        return this.userNameNew;
    }
    
    /** Setter for property userNameNew.
     * @param userNameNew New value of property userNameNew.
     *
     */
    public void setUserNameNew(String userNameNew) throws TextInvalidException {
        TextValidation.isUserName(userNameNew);
        this.userNameNew = userNameNew;
    }
    
}
