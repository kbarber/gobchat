/*
 * UserMessage.java
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
public class UserMessage extends DataBean implements java.io.Serializable {
   
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property userNameSrc. */
    private String userNameSrc;
    
    /** Holds value of property userNameDst. */
    private String userNameDst;
    
    /** Holds value of property message. */
    private String message;
    
    /** Creates new UserMessage */
    public UserMessage() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property userNameSrc.
     * @return Value of property userNameSrc.
     *
     */
    public String getUserNameSrc() {
        return this.userNameSrc;
    }
    
    /** Setter for property userNameSrc.
     * @param userNameSrc New value of property userNameSrc.
     *
     */
    public void setUserNameSrc(String userNameSrc) throws TextInvalidException {
        TextValidation.isUserName(userNameSrc);
        this.userNameSrc = userNameSrc;
    }
    
    /** Getter for property userNameDst.
     * @return Value of property userNameDst.
     *
     */
    public String getUserNameDst() {
        return this.userNameDst;
    }
    
    /** Setter for property userNameDst.
     * @param userNameDst New value of property userNameDst.
     *
     */
    public void setUserNameDst(String userNameDst) throws TextInvalidException {
        TextValidation.isUserName(userNameDst);
        this.userNameDst = userNameDst;
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
