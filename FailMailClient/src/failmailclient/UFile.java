/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailclient;

import java.net.InetAddress;

/**
 *
 * @author mirka
 */
public class UFile {
    private final String name;
    private final String type;
    private final int size;
    private final String lastModifiedDate;
    private final InetAddress ip;
    private final int port;

    public UFile(String name, String type, int size, String lastModifiedDate, InetAddress ip, int port) {
        this.port = port;
        this.ip = ip;
        this.name = name;
        this.type = type;
        this.size = size;
        this.lastModifiedDate = lastModifiedDate;
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
        return "<"+this.name+", "+this.type+", "+this.lastModifiedDate+", "+this.size+", " + this.getIp() + ", " + this.getPort() + ">";
    }

    /**
     * @return the ip
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }
    
    
}
