/*
 * MakeConfig.java
 *
 * Created on 29 October 2003, 21:55
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
        
        Logging log = new Logging();
        log.setLogFile("/var/log/gob/gobchat.log");
        log.setLogLevel("FINEST");
        
        ServerConfiguration sc = new ServerConfiguration();
        sc.setVersion("0.3");
        sc.setTCPPort((short)6666);
        sc.setNetwork(network);
        sc.setLogging(log);
        
        
        ServerConfigurationDAO.write(args[0], sc);
    }
    
}
