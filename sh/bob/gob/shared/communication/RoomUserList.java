/*
 * RoomUserList.java
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
public class RoomUserList extends DataBean implements java.io.Serializable {
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property roomName. */
    private String roomName;
    
    /** Holds value of property filter. */
    private String filter;
    
    /** Holds value of property users. */
    private UserItem[] users;
    
    /** Creates new RoomUserList */
    public RoomUserList() {
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
    
    /** Getter for property filter.
     * @return Value of property filter.
     *
     */
    public String getFilter() {
        return this.filter;
    }
    
    /** Setter for property filter.
     * @param filter New value of property filter.
     *
     */
    public void setFilter(String filter) throws TextInvalidException {
        TextValidation.isSearch(filter);
        this.filter = filter;
    }
    
    /** Getter for property users.
     * @return Value of property users.
     *
     */
    public UserItem[] getUsers() {
        return this.users;
    }
    
    /** Setter for property users.
     * @param users New value of property users.
     *
     */
    public void setUsers(UserItem[] users) {
        this.users = users;
    }
    
}
