/*
 * NameChange.java
 *
 * Created on 1 November 2003, 21:54
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
