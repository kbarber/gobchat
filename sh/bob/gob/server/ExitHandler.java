/*
 * ExitHandler.java
 *
 * Created on 12 October 2003, 13:09
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

package sh.bob.gob.server;

import java.util.logging.*;

/**
 *
 * @author  ken
 */
public class ExitHandler extends Thread {
    
    /** Creates a new instance of ExitHandler */
    public ExitHandler() {
    }
    
    public void run() {
        Logger.getLogger("sh.bob.gob.server").info("Exiting Gob Online Chat");
    }
    
}
