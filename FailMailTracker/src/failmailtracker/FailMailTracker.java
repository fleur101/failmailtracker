/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailtracker;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;


/**
 *
 * @author mirka
 */
public class FailMailTracker {
    
    public static List<Peer> peers = new ArrayList();
    public static List<File> files = new ArrayList();
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception{
    
        ServerSocket msocket = new ServerSocket(39393);
        System.out.println("Listening at port 39393");
     /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UI().setVisible(true);
            }
        });
        while (true){
            Socket connectionSocket = msocket.accept();
            System.out.println("Client arrived!");
            Peer client = new Peer(connectionSocket);
            Thread thread = new Thread(client);
            thread.start();
            peers.add(client);
            System.out.println("Move next");            
        }
    }
    
}
