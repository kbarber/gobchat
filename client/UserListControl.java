/*
 * UserListControl.java
 *
 * Created on 9 January 2003, 21:18
 */

package client;

import javax.swing.*;
import java.util.*;


/**
 *
 * @author  Ken Barber
 */
public class UserListControl {
    
    private JList UserList;
        
    /** Creates a new instance of UserListControl */
    public UserListControl(JList jl) {
        UserList = jl;
        
    }
    
    public synchronized void addUser(String user) {
            
        ArrayList userlist = buildAL();
        
        userlist.add(user);
            
        UserList.setListData(userlist.toArray());
    }
    
    public synchronized void deleteUser(String user) {
        ArrayList userlist = buildAL();
        
        userlist.remove(userlist.indexOf(user));
        
        UserList.setListData(userlist.toArray());
    }
    
    public synchronized void clearList() {
        Vector blank = new Vector();
        
        UserList.setListData(blank);
    }
    
    private ArrayList buildAL() {
        // Create a new ArrayList
        ArrayList al = new ArrayList();
        
        // Calculate the amount of items in the JList
        int jlLength = UserList.getModel().getSize();
        
        // Take the JList ListModel and cycle through it
        // adding each item to the ArrayList
        if(jlLength > 0) {
            for(int ind = 0; ind < jlLength; ind++) {
                al.add(UserList.getModel().getElementAt(ind));
            }
        }
                
        // Return the ArrayList
        return al;
    }
    
}
