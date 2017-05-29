/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailtracker;

import static failmailtracker.FailMailTracker.files;
import static failmailtracker.FailMailTracker.peers;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
/**
 *
 * @author mirka
 */
public class Peer implements Runnable {
    private final Socket socket;
    
    public Peer(Socket socket){
        this.socket=socket;
    }
    
    synchronized public void die(){
        try {
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Error closing socket");
        }
        int i = 0;
        for (Iterator<failmailtracker.File> iterator = files.iterator(); iterator.hasNext(); ) {
            failmailtracker.File value = iterator.next();
            if (value.getLocalPort() == this.socket.getLocalPort()) {
                iterator.remove();
            }
        }
        peers.remove(this);
    }
    
    public void search(String str) throws IOException{
        String response = "";
        for (File file: files){
            if (file.getName().toLowerCase().contains(str.toLowerCase())){
                response+=file.toString();
            }
        }
        response = "".equals(response)?"NOT FOUND\n":("FOUND: " + response + "\n");
        System.out.println(response);
        this.socket.getOutputStream().write(response.getBytes("utf-8"));
    }
    
    @Override
    public void run(){
        BufferedReader inFromClient = null;
        
        //HANDSHAKE BLOCK
        try {
            this.socket.getInputStream();
            inFromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String handshake;
            handshake = inFromClient.readLine();
            if (handshake.trim().compareTo("HELLO")!=0){
                throw new IOException("no hello");
            }
            this.socket.getOutputStream().write("HI\n".getBytes("UTF-8"));
            String recordsPlain;
            recordsPlain = inFromClient.readLine();
            recordsPlain = recordsPlain.substring(1);
            recordsPlain = recordsPlain.substring(0, recordsPlain.length()-1);
            String[] records = recordsPlain.split("><");
            for (String record: records){
                String[] items = record.split(", ");
                files.add(new File(items[0], items[1], Integer.parseInt(items[2]), items[3], socket.getInetAddress(), Integer.parseInt(items[4]), socket));
            }
        } catch (IOException ex) {
            System.out.println("Error handshake");
            System.out.println(ex.getMessage());
            this.die();
            return;
        } catch (NumberFormatException ex){
            System.out.println(ex.getMessage());
        }
        while(true){
            try {
                String request = inFromClient.readLine();
                if (request == null){
                    break;
                }
                System.out.println(this.socket.getPort() + ": " + request);
                if (request.trim().compareToIgnoreCase("bye") == 0){
                        break;
                } else
                if (request.indexOf("SEARCH: ") == 0){
                    String filename = request.substring(8);
                    this.search(filename);
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
                break;
            }
        }
        this.die();
    }
}
