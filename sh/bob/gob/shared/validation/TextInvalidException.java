/*
 * TextInvalidException.java
 *
 * Created on 28 October 2003, 21:19
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

package sh.bob.gob.shared.validation;

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
