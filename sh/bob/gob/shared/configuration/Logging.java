/*
 * Logging.java
 *
 * Created on 1 November 2003, 19:20
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

package sh.bob.gob.shared.configuration;

import java.beans.*;

/**
 *
 * @author  Ken Barber
 */
public class Logging extends Object implements java.io.Serializable {
    
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property logFile. */
    private String logFile;
    
    /** Holds value of property logLevel. */
    private String logLevel;
    
    /** Creates new Logging */
    public Logging() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property logFile.
     * @return Value of property logFile.
     *
     */
    public String getLogFile() {
        return this.logFile;
    }
    
    /** Setter for property logFile.
     * @param logFile New value of property logFile.
     *
     */
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }
    
    /** Getter for property logLevel.
     * @return Value of property logLevel.
     *
     */
    public String getLogLevel() {
        return this.logLevel;
    }
    
    /** Setter for property logLevel.
     * @param logLevel New value of property logLevel.
     *
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
}
