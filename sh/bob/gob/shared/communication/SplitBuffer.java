/*
 * SplitBuffer.java
 *
 * Created on 5 December 2003, 22:46
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

import java.beans.*;
import java.util.*;

/**
 *
 * @author  ken
 */
public class SplitBuffer extends Object implements java.io.Serializable {
    
    /** Holds value of property timeStamp. */
    private Long timeStamp;
    
    /** Holds value of property splitBuffer. */
    private byte[] splitBuffer;
    
    /** Creates new SplitBuffer */
    public SplitBuffer() {
    }
    
    
    /** Getter for property timeStamp.
     * @return Value of property timeStamp.
     *
     */
    public Long getTimeStamp() {
        return this.timeStamp;
    }
    
    /** Setter for property timeStamp.
     * @param timeStamp New value of property timeStamp.
     *
     */
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    /** Getter for property splitBuffer.
     * @return Value of property splitBuffer.
     *
     */
    public byte[] getSplitBuffer() {
        return this.splitBuffer;
    }
    
    /** Setter for property splitBuffer.
     * @param splitBuffer New value of property splitBuffer.
     *
     */
    public void setSplitBuffer(byte[] splitBuffer) {
        this.splitBuffer = splitBuffer;
        this.timeStamp = new Long(new Date().getTime());
    }
    
}
