/*
 * MakeConfig.java
 *
 * Created on 29 October 2003, 21:55
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

package sh.bob.gob.serverconf;

import java.util.HashMap;

import sh.bob.gob.shared.configuration.*;

/**
 *
 * @author  Ken Barber
  */
public class Main {
    
    /** Creates a new instance of MakeConfig */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /* Handle parameters from the command line 
         *
         * [ --factorydefaults <option> --outputfile <file> ]
         * | [ --inputfile <file> ]
         *
         * 
         * 
         * All inputs to switches that contain spaces, must be double quoted (")
         *
         */
        
        /* Can you say, I love HashMap's? */
        HashMap hmParameters = new HashMap();
        
        boolean switchnext = true;
        for(int i = 0; i < args.length; i++) {
            /* Confirm that it is a switch */
            if(args[i].matches("^[-]{1,2}(factorydefaults|outputfile|inputfile)$")) {
                /* Cool, it is a valid switch */
            } else {
                /* Invalid switch, goodbye */
                System.out.println("Invalid switch");
            }
            
            if((args.length) >= (i+1)) {
                /* No more options, bail */
                continue;
            }
            
            if(args[i+1].matches("^-{1,2}")) {
                /* This is another switch, store and then next loop */
                hmParameters.put(args[i],  "");
                
                continue;
            } else {
                /* This is an option */
                hmParameters.put(args[i], args[i+1]);
                i++;
            }
          
        }
        
        /* Wicked, now lets scroll through the keysets, and setup the system */
        String sInputFile = (String)hmParameters.get("--inputfile");
        String sOutputFile = (String)hmParameters.get("--outputfile");
        String sFactoryDefaults = (String)hmParameters.get("--factorydefaults");

        if(!sFactoryDefaults.equals(null) || !sFactoryDefaults.equals("")) {
            if(!sOutputFile.equals(null) || !sOutputFile.equals("")) {
                if(!sFactoryDefaults.equals("windowsxp")) {
                } else if(!sFactoryDefaults.equals("rhlinux")) {
                } else {
                    /* Invalid Factory */
                    System.out.println("Invalid factory");
                }
            }
        }
        
        if(!sInputFile.equals(null) || !sInputFile.equals("")) {
            new ConfigurationApp().show();
        }
        
        Network network = new Network();
        network.setMaxBufferSize(8192);
        network.setMaxObjectSize(1024);
        network.setMaxObjectsInBuffer(8);
        network.setSplitBufferTimeout(300L); // 5 Minutes
//        network.setIdleDisconnectTimeout(300000L); // 5 Minutes
        network.setIdleDisconnectTimeout(60000L); // One minute
//        network.setIdlePingTimeout(100000L); // 1 minute 20 seconds for every Ping to a user
        network.setIdlePingTimeout(10000L); // 10 seconds
        
        Logging log = new Logging();
//        log.setLogFile("/var/log/gob/gobchat.log");
        log.setLogFile("C:\\Program Files\\Gob Online Chat\\log\\gobchat.log");
        log.setLogLevel("FINEST");
        
        ServerConfiguration sc = new ServerConfiguration();
        sc.setVersion("0.4");
        sc.setTCPPort((short)6666);
        sc.setNetwork(network);
        sc.setLogging(log);
        
        
        ServerConfigurationDAO.write(args[0], sc);
    }
    
}
