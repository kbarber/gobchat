/*
 * RoomJoin.java
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
public class RoomJoin extends DataBean implements java.io.Serializable {
       
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property roomName. */
    private String roomName;
    
    /** Holds value of property userName. */
    private String userName;
    
    /** Creates new RoomJoin */
    public RoomJoin() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
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
