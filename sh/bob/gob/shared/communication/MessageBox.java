/*
 * MessageBox.java
 *
 * Created on 10 November 2003, 22:04
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

package sh.bob.gob.shared.communication;

import sh.bob.gob.shared.configuration.*;

import java.beans.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;

/**
 *
 * @author  ken
 */
public class MessageBox {
    
    private XMLDecoder xmlDecoder;
    private ByteBuffer writeBuffer;
    private int maxBufferSize;    
    private Network networkConf;
    
    /** 
     * Creates a new instance of MessageBox 
     *
     * @param netconf Network configuration for MessageBox
     */
    public MessageBox(Network netconf) {
        networkConf = netconf;
        
        /* Create buffers for the XML pipes */
        writeBuffer = ByteBuffer.allocate(networkConf.getMaxBufferSize());
    }
    
    /**
     * This method will decode data from a channel into a JavaBean.
     * <p>
     * @param channel SocketChannel to read the object
     * @param sb A SplitBuffer object containing the old timestamp and data
     * @return An Object, representing the decoded JavaBean
     */
    public Object[] receiveData(SocketChannel channel, SplitBuffer sb) throws IOException {
        
        /* Check the timestamp on the SplitBuffer, and make sure it hasn't
         * expired. */
        if(sb.getTimeStamp() == null) {
            /* There is no timestamp set yet ... */
        } else if(sb.getTimeStamp().longValue() > 
            (new Date().getTime() - networkConf.getSplitBufferTimeout())) {
         
            sb.setSplitBuffer(null);
        }
        
        writeBuffer.clear();

        /* Pull the data into the buffer */
        int result = channel.read(writeBuffer);
        
        /* Is the result EOS? */
        if(result == -1) {
            /* The connection has closed */
            channel.close();
            throw new IOException("EOS has been reached");
        }
        
        Logger.getLogger("sh.bob.gob.shared").finest("Decoding a bean from buffer");
        Object obj[] = decodeBean(writeBuffer, sb);
        
        Logger.getLogger("sh.bob.gob.shared").finest("Now returning the decoded object");
        return obj;
        
    }
    
    /**
     * This method will encode an object and send it down the channel specified.
     * <p>
     * A simple channel.write() is used to send an encoded ByteBuffer directly 
     * to the channel. This ByteBuffer will contain the XML encoded JavaBean.
     * <p>
     * @param channel SocketChannel to send the object
     * @param obj A JavaBean, such as the ones from sh.bob.gob.shared.communication.*
     */
    public void sendData(SocketChannel channel, Object obj) throws ClosedChannelException, IOException {
        Logger.getLogger("sh.bob.gob.shared").finest("Now encoding data");
        ByteBuffer bb = encodeBean(obj);
        Logger.getLogger("sh.bob.gob.shared").finest("Now sending data");
        
        try {
            channel.write(bb);
        } catch (ClosedChannelException ex) {
            Logger.getLogger("sh.bob.gob.shared").warning("Channel has closed, cannot send data.");
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger("sh.bob.gob.shared").warning("Exception while writing bytebuffer to channel: " + ex);
            return;
        }
        Logger.getLogger("sh.bob.gob.shared").finest("Done sending data");
    }    
    
    /**
     * This method decodes a bean from XML contained in a ByteBuffer.
     * <p>
     * I need this because Java doesn't allow me to use XMLEncoder & XMLDecoder
     * using non-blocking IO.
     * <p>
     * This is kind of a blocking wrapper - its hopefully temporary
     * until Java has nio functionality for java.beans.XML*.
     * <p>
     * @param buffer ByteBuffer to read from
     * @param sb SplitBuffer object containing old data information
     * @return Returns an Object representing the encoded JavaBean
     */
    private Object[] decodeBean(ByteBuffer buffer, SplitBuffer sb) {

        int bufferlength = buffer.position();
        
        buffer.flip();
        
        byte bytespace[] = new byte[bufferlength];
        
        buffer.get(bytespace);
        
        Logger.getLogger("sh.bob.gob.shared").finest("\n\nCompressed Packet DEC   (" + 
            bytespace.length + "): \n" + new String(bytespace) + "\n");
        
        /* Take the SplitBuffer and prepend this to the downloaded buffer */
        if(sb.getSplitBuffer() != null) {
            Logger.getLogger("sh.bob.gob.shared").finest("Prepending split buffer (" + sb.getSplitBuffer().length + "): \n" + new String(sb.getSplitBuffer()));
            bytespace = (new String(sb.getSplitBuffer()) + new String(bytespace)).getBytes();
            bufferlength = bytespace.length;
            Logger.getLogger("sh.bob.gob.shared").finest("\nBuffer now contains (" + bufferlength + "): \n" + new String(bytespace) + "\n");
        }
        
        /* Now lets find the double nulls. Cycle through each byte, keep a Vector where
         * we record where each double null is. */
        Vector nullLocations = new Vector();
        int lastNullLocation = -1;
        
        if(bufferlength > 1) { // If the buffer length is 1 or less, then there can't be a double null!
                    
            String rawvalue = "";
            String nullchecks = "";
            
            for(int i = 0; i < (bufferlength - 1); i++) {
                rawvalue = rawvalue + bytespace[i] + ",";
                if(bytespace[i] == 0) {
                    nullchecks += "Found a single null at location: " + i + "\n";
                }
                if((bytespace[i] == 0) && (bytespace[i+1] == 0)) {
                    lastNullLocation = i;
                    nullchecks += "Found a double null at location: " + lastNullLocation + "\n";
                    nullLocations.add(new Integer(lastNullLocation));
                }
            }
            rawvalue = rawvalue + bytespace[bufferlength - 1];
            
            Logger.getLogger("sh.bob.gob.shared").finest(
                "\n\nCompressed Packet ASCII (" + bufferlength + "): " + rawvalue + 
                "\n" + nullchecks +
                "Double Nulls: " + nullLocations.size() + "\n");
        }
        
        /* Okay, if there is no nulls - then just return the bufferdata (if there is any) */
        if(nullLocations.size() == 0) {
            if(bytespace.length > 0) {
                sb.setSplitBuffer(bytespace);
            } else {
                sb.setSplitBuffer(null);
            }
            return new Object[] {sb};
        }

        /* We'll start by calculating the remaining data, if it is empty,
         * set the splitbuffer to null and add the split buffer to the return 
         * Vector */
        Vector returnArrayVector = new Vector();
        if(lastNullLocation == (bytespace.length - 2)) {
            /* The last double null is at the end of the buffer, so there is no split */
            sb.setSplitBuffer(null);
            returnArrayVector.add(sb);
        } else {
            /* The last null is not at the end, so we need to grab the end and
             * save it in the splitbuffer */
            
            int startSplit = lastNullLocation + 2;
            int endSplit = bytespace.length - 1;

            sb.setSplitBuffer(extractByteArray(bytespace, startSplit, endSplit));
            returnArrayVector.add(sb);
        }
         
        /* Now we need to parse through each block of data with the XMLDecoder
         * and hopefully build up an array of valid DataBeans ... */
        
        /* Cycle through each null location */
        int startinglocation = 0; /* This is the first block to start extracting from */
        for(int i = 0; i < nullLocations.size(); i++) {
            int nulllocation = ((Integer)nullLocations.get(i)).intValue(); 

            if((nulllocation - 1) < startinglocation) {
                Logger.getLogger("sh.bob.gob.shared").warning("Cannot extract this data (" + startinglocation + "," + (nulllocation-1) + ")");
                continue;
            }
            
            byte[] objectdata = extractByteArray(bytespace, startinglocation, nulllocation - 1);
            
            Logger.getLogger("sh.bob.gob.shared").finest("Compressed data block size: " + objectdata.length);
            
            
            /* Now decompress */
            Inflater decompresser = new Inflater();
            decompresser.setInput(objectdata);
            byte[] tempbarray = new byte[8192];
            int tempblength = 0;
            try {
                tempblength = decompresser.inflate(tempbarray);
            } catch(Exception ex) {
                Logger.getLogger("sh.bob.gob.share").warning("Caught exception during decompression: " + ex);
            }
            decompresser.end();
            
            if(tempblength == 0) {
                Logger.getLogger("sh.bob.gob.shared").warning("The decompression returned nothing.");
                continue;
            }
            
            byte decompbarray[] = extractByteArray(tempbarray, 0, tempblength - 1);
            
            if(decompbarray == null) {
                Logger.getLogger("sh.bob.gob.shared").severe("The returned decompbarray is null");
                continue;
            }
            
            Logger.getLogger("sh.bob.gob.shared").finest("\n\nDecompressed data XML (" + tempblength + "): \n" + new String(decompbarray) + "\n");
            
            startinglocation = nulllocation + 2;
            
            ByteArrayInputStream is = new ByteArrayInputStream(decompbarray);
            
            xmlDecoder = new XMLDecoder(is, null, new exceptionListener());
            
            Object obj = new Object();
            
            try {
                obj = xmlDecoder.readObject();
            } catch (NoSuchElementException ex) {
                Logger.getLogger("sh.bob.gob.shared").log(Level.WARNING, "No such element", ex);
            } catch (ArrayIndexOutOfBoundsException ex) {
                /* If the XML JavaBean contains no objects */
                Logger.getLogger("sh.bob.gob.shared").warning("The protocol stream contains no more objects, was the XML packet malformed?");
            }
            
            xmlDecoder.close();
            
            returnArrayVector.add(obj);

        }
        
        Logger.getLogger("sh.bob.gob.shared").finest("Return array of objects");
        return returnArrayVector.toArray();
    }
    
    /**
     * This method encodes a bean into XML contained in a ByteBuffer.
     * <p>
     * I need this because Java doesn't allow me to use XMLEncoder & XMLDecoder
     * using non-blocking IO.
     * <p>
     * This is kind of a blocking wrapper, not very speed efficient - its temporary
     * until Java has nio functionality for java.beans.XML*.
     * <p>
     * @param obj JavaBean to encode into XML
     */
    private ByteBuffer encodeBean(Object obj) {

        /* Output the XML output of the object to a ByteArrayOutputStream */
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        
        XMLEncoder xmlEncoder = new XMLEncoder(baOutputStream);
        xmlEncoder.setExceptionListener(new exceptionListener());
        xmlEncoder.writeObject(obj);
        xmlEncoder.close();
        
        byte barray[] = baOutputStream.toByteArray();
        
        /* Now compress */
        Deflater compressor = new Deflater(Deflater.FILTERED);
        compressor.setInput(barray);
        compressor.finish();
        byte tempbarray[] = new byte[barray.length];
        int tempblength = compressor.deflate(tempbarray);
        byte compbarray[] = extractByteArray(tempbarray, 0, tempblength - 1, 2);
        
        /* Append a double null */
        compbarray[compbarray.length - 1] = (byte)0;
        compbarray[compbarray.length - 2] = (byte)0;
        
        /* Lets pull out the rawvalue for debugging */
        String rawvalue = "";
        for(int i = 0; i < compbarray.length - 1; i++) {
            rawvalue += compbarray[i] + ",";
        }
        rawvalue += compbarray[compbarray.length - 1];
        
        /* Print out the raw packet */
        Logger.getLogger("sh.bob.gob.shared").finest(
            "\n\nUncompressed Packet XML (" + barray.length + "): \n" + new String(barray) +    
            "\n\nCompressed Packet ASCII (" + compbarray.length + "): \n" + new String(compbarray) +
            "\n\nCompressed Packet DEC   (" + compbarray.length + "): \n" + rawvalue +
            "\n");
        
        return ByteBuffer.wrap(compbarray);
    }
    
    private byte[] extractByteArray(byte[] ba, int start, int end) {
        return extractByteArray(ba, start,  end, 0);
    }
    
    private byte[] extractByteArray(byte[] ba, int start, int end, int buffer) {
            int lengthBA = (end - start) + 1;
            
            if((lengthBA + buffer) < 1) {
                Logger.getLogger("sh.bob.gob.shared").warning("Array parameters to extract are invalid - " + start + "," + end + "," + buffer);
                return null;
            }
            
            byte[] newbuffer = new byte[lengthBA + buffer];
            
            for(int i = start; i <= end; i++) {
                newbuffer[i-start] = ba[i];
            }
            
            return newbuffer;
    }
    
}

class exceptionListener implements ExceptionListener {
 
    public exceptionListener() {
    }
    
    public void exceptionThrown(Exception ex) {
        Logger.getLogger("sh.bob.gob.shared").warning("Problem with decoding/encoding: " + ex);
    }
    
}
