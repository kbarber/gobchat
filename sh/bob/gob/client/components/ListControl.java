/*
 * ListControl.java
 *
 * Created on 9 January 2003, 21:18
 */

package sh.bob.gob.client.components;

import javax.swing.*;
import java.util.*;


/**
 * This object controls a user list in a chat panel.
 *
 * This class takes an existing JList during construction, and allows you to
 * add, delete and remove users.
 *
 * @author  Ken Barber
 */
public class ListControl {
    
    private JList UserList;
        
    /** 
     * Creates a new instance of ListControl. 
     *
     * @param jl The GUI's JList object for which this object will control.
     */
    public ListControl(JList jl) {
        UserList = jl;
    }
    
    /**
     * Add a new user, updating the list.
     *
     * @param user The username to add
     */
    public synchronized void addUser(String user) {
            
        ArrayList userlist = buildAL();
        
        userlist.add(user);
            
        UserList.setListData(userlist.toArray());
    }
    
    /**
     * Delete a user from the list.
     *
     * @param user The username to delete
     */
    public synchronized void deleteUser(String user) {
        ArrayList userlist = buildAL();
        
        userlist.remove(userlist.indexOf(user));
        
        UserList.setListData(userlist.toArray());
    }
    
    /**
     * Check if the user exists.
     *
     * @param user The username of the user
     */
    public boolean checkUser(String user) {
        ArrayList userlist = buildAL();
        
        return userlist.contains(user);
    }
    
    /**
     * Empty the list of users. Thread safe.
     */
    public synchronized void clearList() {
        Vector blank = new Vector();
        
        UserList.setListData(blank);
    }
    
    /**
     * Replace list. Thread safe.
     */
    public synchronized void replaceList(String list[]) {
        UserList.setListData(list);
    }
    
    /**
     * Build an arraylist out of the contents of the JList. This will be used
     * when adding and deleting users.
     */
    private ArrayList buildAL() {
        /* Create a new empty ArrayList */
        ArrayList al = new ArrayList();
        
        /* Calculate the amount of items in the JList */
        int jlLength = UserList.getModel().getSize();
        
        /* 
         * Take the JList ListModel and cycle through it
         * adding each item to the ArrayList
         */
        if(jlLength > 0) {
            for(int ind = 0; ind < jlLength; ind++) {
                al.add(UserList.getModel().getElementAt(ind));
            }
        }
                
        /* Return the ArrayList */
        return al;
    }
    
}
