
package com.fortune.smgw.socket.server;
import java.nio.channels.SocketChannel;

public class ServerData
{
    public SocketServer server;
    public SocketChannel socket;
    public byte[] data;

    public ServerData(SocketServer server, SocketChannel socket, byte[] data) {
        this.server = server;
        this.socket = socket;
        this.data = data;
    }
}
