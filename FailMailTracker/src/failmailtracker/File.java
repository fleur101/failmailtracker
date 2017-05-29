/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailtracker;

import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author mirka
 */
public class File {
    private final String name;
    private final String type;
    private final int size;
    private final String lastModifiedDate;
    private final Socket socket;
    private final InetAddress ip;
    private final int port;

    public File(String name, String type, int size, String lastModifiedDate, InetAddress ip, int port, Socket socket) {
        this.port = port;
        this.ip = ip;
        this.name = name;
        this.type = type;
        this.size = size;
        this.lastModifiedDate = lastModifiedDate;
        this.socket = socket;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    @Override
    public String toString() {
        byte[] b = this.ip.getAddress();
        String strIP = b[0]+"."+b[1]+"."+b[2]+"."+b[3];
        return "<"+this.name+", "+this.type+", "+this.size+", "+this.lastModifiedDate+", " + strIP + ", " + this.port + ">";
    }

    /**
     * @return the socket
     */
    public int getLocalPort() {
        return socket.getLocalPort();
    }
    
}
