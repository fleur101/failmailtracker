/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailclient;

import static failmailclient.FailMailClient.uploadPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author fleur101
 */
public class Sender implements Runnable {

    private final Socket socket;
    //private char[] key;

    Sender(Socket connectionSocket) {
        this.socket = connectionSocket;
    }
//       private void ksa(char[] state)
//    {
//       int i,j=0,t;
//
//       for (i=0; i < 256; ++i)
//          state[i] = (char) i;
//       for (i=0; i < 256; ++i) {
//          j = (j + state[i] + key[i % key.length]) % 256;
//          t = state[i];
//          state[i] = state[j];
//          state[j] =  (char) t;
//       }
//    }
//    
//    private void prga( char state[], char out[])
//    {
//       int i=0,j=0,x,t;
//
//       for (x=0; x < out.length; ++x)  {
//          i = (i + 1) % 256;
//          j = (j + state[i]) % 256;
//          t = state[i];
//          state[i] = state[j];
//          state[j] = (char) t;
//          out[x] = state[(state[i] + state[j]) % 256];
//       }
//    }

//    private byte[] encrypt(byte[] buffer, int len) {
//        char[] state = new char[256];
//        char[] keystream = new char[len];
//        ksa(state);
//        prga(state, keystream);
//        System.out.println(len);
//        for (int i = 0; i<len; i++){
//            buffer[i] = (byte) (buffer[i]^keystream[i]);
//        }
//        return buffer;
//    }   
    @Override
    public void run() {
//         try {
//            BufferedReader inFromSender = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
//            BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A63A3620FFFFFFFFFFFFFFFF", 16);
//            BigInteger g = new BigInteger("2", 10);
//            BigInteger b = BigInteger.probablePrime(100, new Random());
//            BigInteger B = g.modPow(b, p);
//            BigInteger A;
//            socket.getOutputStream().write((B.toString()+"\n").getBytes("UTF-8"));
//            String alice = inFromSender.readLine();
//            A = new BigInteger(alice, 10);
//            key = A.modPow(b, p).toString().toCharArray();
//        } catch (IOException ex) {
//            System.out.println("Exception:" + ex);
//        }

        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String[] input = inFromClient.readLine().split(" ", 2);
            if (input[0].trim().compareTo("DOWNLOAD:") != 0) {
                throw new IOException("does not follow the protocol");
            }
            System.out.println("lol");
            File inputFile = new File(uploadPath + input[1]);
            File encryptedFile = new File("document.encrypted");
            String key = "Mary has one cat";
            try {
                CryptoUtils.encrypt(key, inputFile, encryptedFile);
            } catch (CryptoException ex) {
                System.out.println(ex.getMessage());
            }
            try (OutputStream os = socket.getOutputStream()) {
            //     System.out.println(uploadPath+input[1]);
                 FileInputStream fis = new FileInputStream(encryptedFile);
                 byte[] buffer = new byte[8192];
                 int count;
                 while ((count = fis.read(buffer, 0, buffer.length)) > 0) {
                     System.out.println("read from file: "+ count);
                     os.write(buffer, 0, count);
                     os.flush();
                 }   
            
             fis.close();
              os.close();
            socket.close();

        } catch (IOException ex) {
                System.out.println("Error sending file: "+ex.getMessage());    
        }

    } catch (IOException ex){
        
    }

}
}
