/*
 * Main.java
 *
 * Created on 7 December 2002, 00:29
 */

package server;

/**
 * Primary class for the Gob server. This class spawns the ConnectionControl
 * class and is only used as a starting point.
 *
 * The class also contains some simple high-level methods.
 *
 * @author  Ken Barber
 */
public final class Main {
        
    /**
     * Instance of ConnectionControl.
     */
    private ConnectionControl cc;
    
    /** 
     * Creates a new instance of Main. This creates a new instance of ConnectionControl
     * which contains the main body of code.
     */
    public Main() {
        /* Create a new ConnectionControl */
        cc = new ConnectionControl();
 
    }
    
    /**
     * Output time-stamped text to the console.
     *
     * @param message Output string
     */
    protected static void consoleOutput(String message) {
        System.out.println(new java.util.Date().toString() + ": " + message);
    }
    
    /**
     * Close the program, and output reason to console.
     *
     * @param message Output Error
     */
    protected static void programExit(String message) {
        consoleOutput("Quitting: " + message);
        System.exit(1);
    }
    
    /**
     * Where it all starts.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main();
    }
    
}

