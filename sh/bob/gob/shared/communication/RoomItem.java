/*
 * RoomItem.java
 *
 * Created on 1 November 2003, 21:56
 */

/*
    Copyright (C) 2003,2004 Ken Barber
 
    This file is part of Gob Online Chat.

    Gob Online Chat is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    any later version.

    Gob Online Chat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Gob Online Chat; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
