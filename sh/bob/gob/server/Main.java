/*
 * Main.java
 *
 * Created on 7 December 2002, 00:29
 */

package sh.bob.gob.server;

import sh.bob.gob.shared.configuration.*;

import java.util.Date;

/**
 * Primary class for the Gob server. This class spawns the ConnectionControl
 * class only and catches any exceptions.
 *
 * <p>A standard message output is also provided by this class.
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
    public Main(String configfile) {
        /* Inform of startup on console */
        consoleOutput("Connection control: Starting");
        
        ServerConfiguration sc = ServerConfigurationDAO.read(configfile);
        
        /* Create a new ConnectionControl */
        try {
            cc = new ConnectionControl(sc);
        } catch (Exception ex) {
            /* Catch all exceptions */
            consoleOutput("Connection control: Exception: " + ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * Output a time-stamped message to standard output. This is the standard method
     * for output.
     *
     * This output should be collected and piped to syslog, event viewer or
     * perhaps a file.
     *
     * @param message Output string to display to standard output.
     */
    protected static void consoleOutput(String message) {
        System.out.println(new Date().toString() + ": " + message);
    }
    
    /**
     * Close the program, and output the given reason to the console.
     *
     * @param message Output string to display to standard output.
     */
    protected static void programExit(String message) {
        consoleOutput("Clean Program Exit: " + message);
        
        /* Exit with a zero */
        System.exit(0);
    }
    
    /**
     * Where it all starts. This is the first point of entry.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Inform of startup on console */
        consoleOutput("Starting Gob Online Chat on port " + args[0]);

        /* Trace method calls or instructions - debugging */
//        Runtime.getRuntime().traceMethodCalls(true);
//        Runtime.getRuntime().traceInstructions(true);
        
        /* Register exit handler, deals with terminated process */
        ExitHandler exithandler = new ExitHandler();
        Runtime.getRuntime().addShutdownHook(exithandler);
                
        /* Start a new object */
        new Main(args[0]);
    }
    
}

