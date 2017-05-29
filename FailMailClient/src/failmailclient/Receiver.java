/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailclient;

import static failmailclient.FailMailClient.downloadPath;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author fleur101
 */
public class Receiver implements Runnable {

    private final Socket socket;
    private final String fileName;
 //   private char[] key;

    public Receiver(Socket socket, String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    public void die() {
        try {
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Error closing socket");
        }
    }

    public void getFile() {

        try {
            File outputFile = new File("document.encrypted");
            int i = 0;
            while (!outputFile.createNewFile()) {
                outputFile = new File( "(" + i + ") " + "document.encrypted");
                i++;
            }
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                BufferedOutputStream out = new BufferedOutputStream(fos);
                byte[] buffer = new byte[8192];
                int count;
                InputStream in = socket.getInputStream();
                while ((count = in.read(buffer, 0, buffer.length)) > 0) {
                    out.write(buffer, 0, count);
                    out.flush();
                }
                out.close();
                fos.close();
            }
            File decryptedFile = new File(downloadPath + fileName);
            i = 0;
            while (!decryptedFile.createNewFile()) {
                decryptedFile = new File(downloadPath+ "(copy" + i + ") " + fileName);
                i++;
            }
            String key = "Mary has one cat";
            try {
                CryptoUtils.decrypt(key, outputFile, decryptedFile);
            } catch (CryptoException ex) {
                System.out.println(ex.getMessage());
            }
            socket.close();
        }   catch (IOException ex) {
            System.out.println("Error getting file" + ex.getMessage());
        }
        
        System.out.println("Done downloading!");
        
    }

        @Override
        public void run () {
//        try {
//            BufferedReader inFromSender = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
//            BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A63A3620FFFFFFFFFFFFFFFF", 16);
//            BigInteger g = new BigInteger("2", 10);
//            BigInteger a = BigInteger.probablePrime(100, new Random());
//            BigInteger A = g.modPow(a, p);
//            BigInteger B;
//            socket.getOutputStream().write((A.toString()+"\n").getBytes("UTF-8"));
//            String bob = inFromSender.readLine();
//            B = new BigInteger(bob, 10);
//            key = B.modPow(a, p).toString().toCharArray();
//        } catch (IOException ex) {
//            System.out.println("Exception:" + ex);
//        }
//        
        
        String request = "DOWNLOAD: " + fileName + "\n";
            try {
                socket.getOutputStream().write(request.getBytes("UTF-8"));
                getFile();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            die();

        }

    

//    private void ksa(char[] state) {
//        int i, j = 0, t;
//
//        for (i = 0; i < 256; ++i) {
//            state[i] = (char) i;
//        }
//        for (i = 0; i < 256; ++i) {
//            j = (j + state[i] + key[i % key.length]) % 256;
//            t = state[i];
//            state[i] = state[j];
//            state[j] = (char) t;
//        }
//    }

//    private void prga(char state[], char out[], int len) {
//        int i = 0, j = 0, x, t;
//
//        for (x = 0; x < len; ++x) {
//            i = (i + 1) % 256;
//            j = (j + state[i]) % 256;
//            t = state[i];
//            state[i] = state[j];
//            state[j] = (char) t;
//            out[x] = state[(state[i] + state[j]) % 256];
//        }
//    }
//
//    private byte[] decrypt(byte[] buffer, int len) {
//        char[] state = new char[256];
//        char[] keystream = new char[len];
//        ksa(state);
//        prga(state, keystream, len);
//        for (int i = 0; i < len; i++) {
//            buffer[i] = (byte) (buffer[i] ^ keystream[i]);
//        }
//        return buffer;
//    }

}
