/*
 * TextValidation.java
 *
 * Created on 27 October 2003, 22:42
 */

package sh.bob.gob.shared;

/**
 * <p>This is a package of static test methods. This is specifically used by routines 
 * that require validation of input, such as in a packet or typed in by a user.
 *
 * <p>This package uses the standard Java regex library extensively. Each routine
 * returns a boolean to indicate if the fed parameter patches.
 *
 * @author  Ken Barber
 */
public class TextValidation {
    
    /** Creates a new instance of TextValidation */
    public TextValidation() {
    }

    /**
     * Returns true if the user name is valid.
     *
     * @param name User name to test
     * @return A boolean, true if the user name is valid
     */
    public static boolean isUserName(java.lang.String name) {
        /* Alphanumeric + - no spaces, 3 - 15 characters */
        
        /* Must allow for bigger user names */
        /* Must allow for usernames of one letter */
        
        return false;
    }

    /**
     * Returns true if the room name is valid.
     *
     * @param name Room name to test
     * @return A boolean, true if the room name is valid
     */
    public static boolean isRoomName(java.lang.String name) {
        /* Alphanumeric no spaces, 1-32 characters */
        
        /* Should allow spaces ... but no starting or trailing perhaps? */
        
        return false;
    }
    
    /**
     * Returns true if the message is valid.
     *
     * @param message Message text to test
     * @return A boolean, true if the message is valid
     */
    public static boolean isMessage(java.lang.String message) {
        /* Most chars, 0 - 512 characters */
        
        /* Messages shouldn't be 0 characters. Lets save bandwidth and not permit
         * them */
        
        return false;
    }

    /**
     * Returns true if the quit reason is valid.
     *
     * @param message Message text to test
     * @return A boolean, true if the quit reason is valid
     */
    public static boolean isQuitReason(java.lang.String message) {
        /* Most chars, 0 - 64 characters*/
        
        /* Quit reasons can be 0 chars. But shouldn't be null. */
        
        return false;
    }
}
