/*
 * UserMessage.java
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
