/*
 * SignOff.java
 *
 * Created on 1 November 2003, 21:55
 */

package sh.bob.gob.shared.communication;

import sh.bob.gob.shared.validation.*;

import java.beans.*;

/**
 *
 * @author  ken
 */
public class SignOff extends DataBean implements java.io.Serializable {
   
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property userName. */
    private String userName;
    
    /** Holds value of property message. */
    private String message;
    
    /** Holds value of property roomName. */
    private String roomName;
    
    /** Creates new SignOff */
    public SignOff() {
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
    
    /** Getter for property roomName.
     * @return Value of property roomName.
     *
     */
    public String getRoomName() {
        return this.roomName;
    }
    
    /** Setter for property roomName.
     * @param roomName New value of property roomName.
     *
     */
    public void setRoomName(String roomName) throws TextInvalidException {
        TextValidation.isRoomName(roomName);
        this.roomName = roomName;
    }
    
}
