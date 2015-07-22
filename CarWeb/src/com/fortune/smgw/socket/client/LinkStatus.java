package com.fortune.smgw.socket.client;

public class LinkStatus
{
  public static final int CLOSE = 0;
  public static final int CONNECTED = 1;
  public static final int LOGINING = 2;
  public static final int LOGINED = 3;
  public static final int UNLINKED = -1;
  public static final int LOGINFAIL = -2;
}
