/*
 * Ping.java
 *
 * Created on 8 February 2004, 00:42
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

/**
 * The Ping DataBean is for an element to ping another element.
 *
 * It contains no information, as it is only used to generate noise to trigger
 * the counter reset in the application.
 *
 * @author  Ken Barber
 */
public class Ping extends DataBean implements java.io.Serializable {
    
    /** Creates new Ping */
    public Ping() {
    }
    
    
}
