/*
 * RoomItem.java
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
public class RoomItem extends Object implements java.io.Serializable {
    
    
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property roomName. */
    private String roomName;
    
    /** Holds value of property users. */
    private int users;
    
    /** Creates new RoomItem */
    public RoomItem() {
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
    public String getRoomName() {
        return this.roomName;
    }
    
    /** Setter for property userName.
     * @param roomName New value of property userName.
     *
     */
    public void setRoomName(String roomName) throws TextInvalidException {
        TextValidation.isRoomName(roomName);
        this.roomName = roomName;
    }
    
    /** Getter for property users.
     * @return Value of property users.
     *
     */
    public int getUsers() {
        return this.users;
    }
    
    /** Setter for property users.
     * @param users New value of property users.
     *
     */
    public void setUsers(int users) {
        this.users = users;
    }
    
}
