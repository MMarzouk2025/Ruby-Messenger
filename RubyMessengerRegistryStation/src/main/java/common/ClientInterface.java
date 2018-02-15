/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Message;
import model.User;

/**
 *
 * @author toshiba
 */
public interface ClientInterface extends Remote {

    //public boolean receive(Message msg  , User sender) throws RemoteException;
    //Esraa Hassan
    User getUser() throws RemoteException;
    
    void setUser(User client) throws RemoteException;

    boolean receive(Message msg) throws RemoteException;

    void recieveAnnouncement(String message) throws RemoteException;
    
    // Mahmoud Marzouk
    void sendFriendRequest(String usernameOrEmail) throws RemoteException;
    
    boolean findClient(String usernameOrEmail) throws RemoteException;
    
    void receiveFriendRequest(User fromUser) throws RemoteException;
}