/*
 * TextInvalidException.java
 *
 * Created on 28 October 2003, 21:19
 */

package sh.bob.gob.shared;

/**
 * <p>This Exception is thrown by the TextValidation class when an item is 
 * invalid.
 *
 * <p>This enables a nice way of retrieving an error message, so that each test
 * can return greater detail (optional) as to why the test failed.
 *
 * @author  Ken Barber
 */
public class TextInvalidException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>TextInvalidException</code> without detail message.
     */
    public TextInvalidException() {
    }
    
    
    /**
     * Constructs an instance of <code>TextInvalidException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TextInvalidException(String msg) {
        super(msg);
    }
}
