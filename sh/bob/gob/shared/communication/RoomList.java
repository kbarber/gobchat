/*
 * RoomList.java
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
public class RoomList extends DataBean implements java.io.Serializable {
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property filter. */
    private String filter;
    
    /** Holds value of property rooms. */
    private RoomItem[] rooms;
    
    /** Creates new RoomList */
    public RoomList() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
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
    
    /** Getter for property rooms.
     * @return Value of property rooms.
     *
     */
    public RoomItem[] getRooms() {
        return this.rooms;
    }
    
    /** Setter for property rooms.
     * @param rooms New value of property rooms.
     *
     */
    public void setRooms(RoomItem[] rooms) {
        this.rooms = rooms;
    }
    
}
