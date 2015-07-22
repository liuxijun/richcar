
package com.fortune.smgw.socket.server;
import java.nio.channels.SocketChannel;

public class ReceiveObj
{
    public String msgId;
    public int msgType;
    public Object obj;
    public SocketServer server;
    public SocketChannel channel;

    public ReceiveObj(SocketServer server, SocketChannel channel, String msgId,
                      int msgType, Object obj) {
        this.server = server;
        this.channel = channel;
        this.msgId = msgId;
        this.msgType = msgType;
        this.obj = obj;
    }
}
