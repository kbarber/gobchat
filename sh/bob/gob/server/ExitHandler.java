/*
 * ExitHandler.java
 *
 * Created on 12 October 2003, 13:09
 */

package sh.bob.gob.server;

/**
 *
 * @author  ken
 */
public class ExitHandler extends Thread {
    
    /** Creates a new instance of ExitHandler */
    public ExitHandler() {
    }
    
    public void run() {
        Main.consoleOutput("Exiting Gob Online Chat");
    }
    
}
