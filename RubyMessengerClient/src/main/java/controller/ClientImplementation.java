/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import common.ClientInterface;
import model.Message;
import model.User;

/**
 *
 * @author khaled
 */
public class ClientImplementation extends UnicastRemoteObject implements ClientInterface{

    private User user;
    
    public ClientImplementation() throws RemoteException{
    }
    
    // mahmoud marzouk 10/02/2018
    
    @Override
    public boolean receive(Message msg) throws RemoteException {
        User sender = msg.getSender();
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
