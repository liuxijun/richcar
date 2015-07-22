/* ChangeStatus - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.fortune.smgw.socket.server;
import java.nio.channels.SocketChannel;

public class ChangeStatus
{
    public static final int REGISTER = 1;
    public static final int CHANGEOPS = 2;
    public SocketChannel socket;
    public int type;
    public int ops;

    public ChangeStatus(SocketChannel socket, int type, int ops) {
        this.socket = socket;
        this.type = type;
        this.ops = ops;
    }
}
