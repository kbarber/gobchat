/*
 * Network.java
 *
 * Created on 1 November 2003, 19:20
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
    
}
