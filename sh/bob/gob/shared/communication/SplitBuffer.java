/*
 * SplitBuffer.java
 *
 * Created on 5 December 2003, 22:46
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
