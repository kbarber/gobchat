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
        ServerConfiguration sc = new ServerConfiguration();
        sc.setVersion("0.3");
        sc.setTCPPort((short)6666);
        
        ServerConfigurationDAO.write(args[0], sc);
    }
    
}
