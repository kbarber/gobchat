/*
 * ServerConfigurationDAO.java
 *
 * Created on 29 October 2003, 21:19
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

package sh.bob.gob.shared.configuration;

import sh.bob.gob.shared.configuration.ServerConfiguration;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.ImageIcon;

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
