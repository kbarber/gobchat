/*
 * ServerConfigurationDAO.java
 *
 * Created on 29 October 2003, 21:19
 */

package sh.bob.gob.shared.configuration;

import sh.bob.gob.shared.configuration.ServerConfiguration;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * This object is responsible for loading and saving the server configuration
 * file.
 *
 * In Java, it is accessed as a JavaBean - ServerConfiguration.
 *
 * @author  Ken Barber
 */
public class ServerConfigurationDAO {
    
    public static ServerConfiguration read(String filename) {
        
        FileInputStream file;
        
        try {
            file = new FileInputStream(filename);
        } catch (Exception ex) {
            return null;
        }
        
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(file));
        Object result = d.readObject();
        d.close();
        
        return (ServerConfiguration)result;
    }
    
    public static void write(String filename, ServerConfiguration config) {
        
        FileOutputStream file;
        
        try {
            file = new FileOutputStream(filename);
        } catch (Exception ex) {
            return;
        }
        
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(file));
        e.writeObject(config);
        e.close();
 
    }
    
}
