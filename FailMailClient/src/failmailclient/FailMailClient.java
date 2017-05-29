/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailclient;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;





/**
 *
 * @author mirka
 */
public class FailMailClient {
    private static List<File> myfiles;
    public static List<UFile> searchResultFiles;
    private static Socket socket;
    public static String downloadPath;
    public static String uploadPath;
    private static BufferedReader serverInput;
    private static ServerSocket lsocket;
    private static String trackerIP;
    
    
    public static void listFiles(File folder) {
        for (File file : folder.listFiles()) {
            if (!file.isDirectory()) {
                myfiles.add(file);
            }
        }
    }
    
    public static void searchFile(String fileToGet){
        //Asking for a file
        searchResultFiles.clear();
        String request="SEARCH: "+fileToGet+"\n";
        try {
            System.out.println(socket);
            socket.getOutputStream().write(request.getBytes("UTF-8"));
            String response= serverInput.readLine();  
            
            System.out.println(response + "\n");
            String[] foundResponse;
            if (response.trim().compareTo("NOT FOUND")!=0){
                foundResponse=response.split(": ");
                String foundRecords=foundResponse[1];
                foundRecords = foundRecords.substring(1);
                foundRecords = foundRecords.substring(0, foundRecords.length()-1);
                String[] records = foundRecords.split("><");
                searchResultFiles.clear();
                for (String record: records){
                    System.out.println("record:"+record);
                    String[] items = record.split(", ");
                    System.out.println(items[4]);
                    String[] ipParts = (items[4]).split("\\.");
                    System.out.println(ipParts.length);
                    byte[] b = new byte[4];
                    for (int i = 0; i<4; i++){
                        b[i] = (byte) Integer.parseInt(ipParts[i]);
                    }
                    
                    searchResultFiles.add(new UFile(items[0], items[1], Integer.parseInt(items[2]), items[3], InetAddress.getByAddress(b), Integer.parseInt(items[5])));
                }
            }
        } catch (IOException ex){
            System.out.println("Exception" + ex);
        } 
//        catch (NumberFormatException | NullPointerException ex){
//            System.out.println(ex.getMessage());
//        }
    }
    
    public static void downloadFile(int index) throws IOException, InterruptedException{
        UFile peerFile=searchResultFiles.get(index);
        Socket peer = new Socket(peerFile.getIp(), peerFile.getPort());
        Receiver rec = new Receiver(peer, peerFile.getName());
        Thread thr = new Thread(rec);
        thr.start();    
    }
    
    public static String getFileInfo(File file) throws UnknownHostException{
        String records;
        String fileName=file.getName();
        String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
        long fileSize=file.length();
        String fileLMD=new SimpleDateFormat("dd-MM-yyyy").format(file.lastModified());
        int port=lsocket.getLocalPort();
        records="<"+fileName+", "+fileType+", "+fileSize +", "+fileLMD+", "+port+">";
        //System.out.println(records);
        return records;
    }
    
    public static int handshake(){
        //HANDSHAKE
        
        System.out.println("Handshake");
        try {
            socket.getOutputStream().write("HELLO\n".getBytes("UTF-8"));
            String response = serverInput.readLine();
            if (response.trim().compareTo("HI")!=0){
                throw new IOException();
            } 
            
            final File folder = new File(uploadPath);
            if (!folder.isDirectory()){
                return 1;
            }
            listFiles(folder);
            String records="";
            for (File file : myfiles) {
                records+=getFileInfo(file);
            }
            records+="\n";
            socket.getOutputStream().write(records.getBytes("UTF-8"));
        } catch (IOException ex) {
            System.out.println("Error establishing connection");
            try {
                socket.close();
            } catch (IOException ex1) {
            }
            return 1;
        }
        return 0;
    }
    
    
    public static void bye() throws IOException{
        socket.getOutputStream().write("bye\n".getBytes("UTF-8"));
        socket.close();
        System.exit(0);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        myfiles = new ArrayList();
        searchResultFiles = new ArrayList();
        
        try {
            lsocket = new ServerSocket(0);
            socket = new Socket(trackerIP, 39393);
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Connection was not established "+ex.getMessage());
            return;
        }

        Listener lstnr = new Listener(lsocket);
        Thread lthr = new Thread(lstnr);
        lthr.start();
        
        System.out.println("Listener started");
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientUI().setVisible(true);
            }
        });
    }
}
