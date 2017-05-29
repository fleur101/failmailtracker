/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailclient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fleur101
 */
public class Listener implements Runnable {
    private final ServerSocket socket;

    public Listener(ServerSocket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
         while (true){
            Socket connectionSocket = null;
             try {
                 connectionSocket = socket.accept();
             } catch (IOException ex) {
                 System.out.println(ex.getMessage());
             }
            System.out.println("Client arrived!");
            Sender client = new Sender(connectionSocket);
            Thread thread = new Thread(client);
            thread.start();          
        }
    }
    
}
