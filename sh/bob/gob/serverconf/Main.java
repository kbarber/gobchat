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

import sh.bob.gob.shared.configuration.*;

/**
 *
 * @author  ken
 */
public class Main {
    
    /** Creates a new instance of MakeConfig */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
