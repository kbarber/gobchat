/*
 * Main.java
 *
 * Created on 7 December 2002, 00:29
 */

package server;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author  Ken Barber
 */
public final class Main {
        
    private ConnectionControl cc;
    
    /** 
     * Creates a new instance of Main.
     */
    public Main() {
        /* Create a new ConnectionControl */
        cc = new ConnectionControl();
 
    }
    
    /**
     * Output time-stamped text to the console
     */
    public static void consoleOutput(String message) {
        System.out.println(new Date().toString() + ": " + message);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new Main();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}

