/*
 * ExitHandler.java
 *
 * Created on 12 October 2003, 13:09
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
