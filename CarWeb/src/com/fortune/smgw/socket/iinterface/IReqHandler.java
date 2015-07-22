package com.fortune.smgw.socket.iinterface;

import com.fortune.smgw.socket.server.SocketServer;
import java.nio.channels.SocketChannel;

public abstract interface IReqHandler
{
  public abstract void Request(SocketServer paramSocketServer, SocketChannel paramSocketChannel, byte[] paramArrayOfByte);
}
