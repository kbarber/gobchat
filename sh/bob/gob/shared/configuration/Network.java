/*
 * Network.java
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
public class Network extends Object implements java.io.Serializable {
    
    private PropertyChangeSupport propertySupport;
    
    /** Holds value of property maxBufferSize. */
    private int maxBufferSize;
    
    /** Holds value of property splitBufferTimeout. */
    private long splitBufferTimeout;
    
    /** Holds value of property maxObjectSize. */
    private int maxObjectSize;
    
    /** Holds value of property maxObjectsInBuffer. */
    private int maxObjectsInBuffer;
    
    /** Holds value of property idlePingTimeout. */
    private long idlePingTimeout;
    
    /** Holds value of property idleDisconnectTimeout. */
    private long idleDisconnectTimeout;
    
    /** Creates new Logging */
    public Network() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property maxBufferSize.
     * @return Value of property maxBufferSize.
     *
     */
    public int getMaxBufferSize() {
        return this.maxBufferSize;
    }
    
    /** Setter for property maxBufferSize.
     * @param maxBufferSize New value of property maxBufferSize.
     *
     */
    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }
    
    /** Getter for property splitBufferTimeout.
     * @return Value of property splitBufferTimeout.
     *
     */
    public long getSplitBufferTimeout() {
        return this.splitBufferTimeout;
    }
    
    /** Setter for property splitBufferTimeout.
     * @param splitBufferTimeout New value of property splitBufferTimeout.
     *
     */
    public void setSplitBufferTimeout(long splitBufferTimeout) {
        this.splitBufferTimeout = splitBufferTimeout;
    }
    
    /** Getter for property maxObjectSize.
     * @return Value of property maxObjectSize.
     *
     */
    public int getMaxObjectSize() {
        return this.maxObjectSize;
    }
    
    /** Setter for property maxObjectSize.
     * @param maxObjectSize New value of property maxObjectSize.
     *
     */
    public void setMaxObjectSize(int maxObjectSize) {
        this.maxObjectSize = maxObjectSize;
    }
    
    /** Getter for property maxObjectsInBuffer.
     * @return Value of property maxObjectsInBuffer.
     *
     */
    public int getMaxObjectsInBuffer() {
        return this.maxObjectsInBuffer;
    }
    
    /** Setter for property maxObjectsInBuffer.
     * @param maxObjectsInBuffer New value of property maxObjectsInBuffer.
     *
     */
    public void setMaxObjectsInBuffer(int maxObjectsInBuffer) {
        this.maxObjectsInBuffer = maxObjectsInBuffer;
    }
    
    /** Getter for property idlePingTimeout.
     * @return Value of property idlePingTimeout.
     *
     */
    public long getIdlePingTimeout() {
        return this.idlePingTimeout;
    }
    
    /** Setter for property idlePingTimeout.
     * @param idlePingTimeout New value of property idlePingTimeout.
     *
     */
    public void setIdlePingTimeout(long idlePingTimeout) {
        this.idlePingTimeout = idlePingTimeout;
    }
    
    /** Getter for property idleDisconnectTimeout.
     * @return Value of property idleDisconnectTimeout.
     *
     */
    public long getIdleDisconnectTimeout() {
        return this.idleDisconnectTimeout;
    }
    
    /** Setter for property idleDisconnectTimeout.
     * @param idleDisconnectTimeout New value of property idleDisconnectTimeout.
     *
     */
    public void setIdleDisconnectTimeout(long idleDisconnectTimeout) {
        this.idleDisconnectTimeout = idleDisconnectTimeout;
    }
    
}
