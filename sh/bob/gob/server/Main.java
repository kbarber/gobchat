/*
 * Main.java
 *
 * Created on 7 December 2002, 00:29
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

import sh.bob.gob.shared.configuration.*;

import java.util.Date;
import java.util.logging.*;


/**
 * Primary class for the Gob server. This class spawns the ConnectionControl
 * class only and catches any exceptions.
 *
 * <p>A standard message output is also provided by this class, utilising the new
 * java.util.logging package.
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
        /* First read the configuration file */
        ServerConfiguration sc = ServerConfigurationDAO.read(configfile);
        
        /* Check if sc is null */
        if(sc == null) {
            System.out.println("Configuration file is invalid.");
            System.exit(1);
        }

        /* Set up logging */
        prepareLogging(sc.getLogging());
        
        /* Server configuration */
        Logger.getLogger("sh.bob.gob.server").config(
            "\n\tConfiguration File: " + configfile +
            "\n\tConfiguration Version: " + sc.getVersion() +
            "\n\tTCP port: " + sc.getTCPPort() +
            "\n\tLog level: " + sc.getLogging().getLogLevel() +
            "\n\tLog file: " + sc.getLogging().getLogFile() +
            "\n\tIdle Ping Timeout: " + sc.getNetwork().getIdlePingTimeout() +
            "\n\tIdle Disconnect Timeout: " + sc.getNetwork().getIdleDisconnectTimeout() +
            "\n\tMax Buffer Size: " + sc.getNetwork().getMaxBufferSize() +
            "\n\tMax Object Size: " + sc.getNetwork().getMaxObjectSize() +
            "\n\tMax Objects in Buffer: " + sc.getNetwork().getMaxObjectsInBuffer() +
            "\n\tSplit Buffer Timeout: " + sc.getNetwork().getSplitBufferTimeout());
        
        /* Startup message log */
        Logger.getLogger("sh.bob.gob.server").info("Connection control: Starting");
        
        /* Loop forever, if we catch an exception - lets just restart. 
         * 
         * No reason to just bail out completely. Cool.
         */
        for (;;) {
            /* Create a new ConnectionControl */
            try {
                cc = new ConnectionControl(sc);
            } catch (Exception ex) {
                /* Catch all exceptions */
                Logger.getLogger("sh.bob.gob.server").log(Level.SEVERE, "Connection control exception", ex);
                ex.printStackTrace();
            }
            
            /* Log that this has happened, I wouldn't mind an email alert? */
            Logger.getLogger("sh.bob.gob.server").severe("Exception caught, respawning a new ConnectionControl.");
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
        Logger.getLogger("sh.bob.gob.server").info("Clean Program Exit: " + message);
        
        /* Exit with a zero */
        System.exit(0);
    }
    
    /**
     * Where it all starts. This is the first point of entry.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /* Trace method calls or instructions - debugging */
//        Runtime.getRuntime().traceMethodCalls(true);
//        Runtime.getRuntime().traceInstructions(true);
        
        /* Check weither the argument given is valid */
        if(args.length == 0) {
                System.out.println("No configuration file specified.");
                System.out.println("Usage: java -jar server.jar <configuration file>");
                System.exit(1);
        }
        
        /* Register exit handler, deals with terminated process */
        ExitHandler exithandler = new ExitHandler();
        Runtime.getRuntime().addShutdownHook(exithandler);
                
        /* Start a new object */
        new Main(args[0]);
    }

    /**
     * This function prepares all logging for the application.
     * <p>
     * We prepare both the server and shared logging facilities for output to
     * a file.
     * <p>
     * @param logconf A Logging bean containing the log output configuration.
     */
    private void prepareLogging(Logging logconf) {
        
        FileHandler fh = null;
        try {
            /* Create a FileHandler to the correct file */
            fh = new FileHandler(logconf.getLogFile(), true);
        } catch (Exception ex) {
            consoleOutput("Failure to open log file, exiting: " + ex);
            System.exit(1);
        }
        
        /* Set the formatter */
        fh.setFormatter(new SimpleFormatter());
        
        /* Create the loggers and add the FileHandler.
         */
        Logger serverlogger = Logger.getLogger("sh.bob.gob.server");
        serverlogger.getParent().setLevel(Level.parse(logconf.getLogLevel()));
        serverlogger.setLevel(Level.parse(logconf.getLogLevel()));
        serverlogger.addHandler(fh);
        
        Logger sharedlogger = Logger.getLogger("sh.bob.gob.shared");
        sharedlogger.getParent().setLevel(Level.parse(logconf.getLogLevel()));
        sharedlogger.setLevel(Level.parse(logconf.getLogLevel()));
        sharedlogger.addHandler(fh);
    }
    
}

