/*
 * TextValidation.java
 *
 * Created on 27 October 2003, 22:42
 */

package sh.bob.gob.shared.validation;

import java.util.regex.Pattern;

/**
 * <p>This is a package of static test methods. This is specifically used by routines 
 * that require validation of input, such as in a packet or typed in by a user.
 *
 * <p>This package uses the standard Java regex library extensively. Each routine
 * throws a TextInvalidException to indicate if the fed parameter patches.
 *
 * <p>The thrown TextInvalidException can be caught and a reason for test failure
 * can be retrieved.
 *
 * @author  Ken Barber
 */
public class TextValidation {
    
    /**
     * Returns true if the user name is valid.
     *
     * @param name User name to test
     * @return A boolean, true if the user name is valid
     */
    public static void isUserName(java.lang.String name) throws TextInvalidException {
        /* Basic chars, spaces, 1 - 30 characters */
        
        /* Must allow for bigger user names */
        /* Must allow for usernames of one letter */
        
        if(Pattern.matches("[ \\a-zA-Z0-9\\[\\]!\"#$%&'()*+,-./:;<=>?@\\^_`{|}~]{1,30}", name) == false)
            throw new TextInvalidException("Invalid characters");
    }

    /**
     * Returns true if the room name is valid.
     *
     * @param name Room name to test
     * @return A boolean, true if the room name is valid
     */
    public static void isRoomName(java.lang.String name) throws TextInvalidException {
        /* Basic chars, spaces, 1 - 30 characters */
        
        /* Should allow spaces ... but no starting or trailing perhaps? */
        
        if(Pattern.matches("[ \\a-zA-Z0-9\\[\\]!\"#$%&'()*+,-./:;<=>?@\\^_`{|}~]{1,30}", name) == false)
            throw new TextInvalidException("Invalid characters");
    }
    
    /**
     * Returns true if the message is valid.
     *
     * @param message Message text to test
     * @return A boolean, true if the message is valid
     */
    public static void isMessage(java.lang.String message) throws TextInvalidException {
        /* Most chars, 0 - 512 characters */
        
        /* Messages shouldn't be 0 characters. Lets save bandwidth and not permit
         * them */
        
        /* If the message is larger than 512 characters - disallow it */
        
        if(message.length() > 512)
            throw new TextInvalidException("Message is too long");

        /* If the message is smaller than 1 character - disallow it */
        
        if(message.length() < 1)
            throw new TextInvalidException("Message cannot be empty");

        /* If the message only contains spaces, tabs, line feeds or carriage returns - disallow it */

        if(Pattern.matches("[ \t\n\r]{1,512}", message))
            throw new TextInvalidException("Message cannot just contain whitespace");
        
//        if(Pattern.matches("[ \\a-zA-Z0-9\t\\[\\]!\"#$%&'()*+,-./:;<=>?@\\^_`{|}~]{1,512}", message) == false)
//            throw new TextInvalidException("Invalid characters in message");
    }

    /**
     * Returns true if the quit reason is valid.
     *
     * @param message Message text to test
     * @return A boolean, true if the quit reason is valid
     */
    public static void isQuitReason(java.lang.String message) throws TextInvalidException {
        /* Most chars, 0 - 64 characters*/
        
        if(Pattern.matches("[ \\a-zA-Z0-9\t\\[\\]!\"#$%&'()*+,-./:;<=>?@\\^_`{|}~]{0,64}", message) == false) 
            throw new TextInvalidException("Invalid characters in message");
    }
    
    /**
     * Returns true if the search string is valid.
     *
     * @param search Search text to test
     * @return A boolean, true if the search string is valid
     */
    public static void isSearch(java.lang.String search) throws TextInvalidException {
        /* Most chars, 0 - 64 characters*/
        
        if(Pattern.matches("[*]{1,1}", search) == false) 
            throw new TextInvalidException("Invalid characters in search");
    }

}
