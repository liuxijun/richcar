package com.fortune.smgw.socket.client;

import com.fortune.smgw.socket.iinterface.ILoginHandler;
import com.fortune.smgw.socket.iinterface.IRspHandler;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class SocketClientLink
        implements Runnable{
    private Logger log = Logger.getLogger(SocketClientLink.class);
    private InetAddress hostAddress;
    private int port;
    public int linkId;
    private SocketChannel socket;
    private Selector selector;
    private Thread selectorThread;
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    public static final int STATUS_NOT_CONNECTED=-2;
    public static final int STATUS_CONNECTED=0;
    public static final int STATUS_LOGINED=1;
    public List sendData = Collections.synchronizedList(new ArrayList());
    private ClientSendThread sendThread;
    private IRspHandler handler;
    private ILoginHandler loginHandler;
    private int status;
    public WaitHandler connectHandler;

    public SocketClientLink(InetAddress hostAddress, int port, IRspHandler handler, int linkId, ILoginHandler loginHandler)
            throws IOException
    {
        this.hostAddress = hostAddress;
        this.port = port;
        this.handler = handler;
        this.loginHandler = loginHandler;
        this.selector = initSelector();

        this.sendThread = new ClientSendThread(linkId);
        this.connectHandler = new WaitHandler();
        this.selectorThread = new Thread(this);
        this.selectorThread.start();

        this.linkId = linkId;
    }

    public void send(byte[] data)
            throws IOException
    {
        this.log.debug("linkId:" + this.linkId + "add data send");
        this.sendData.add(ByteBuffer.wrap(data));
    }

    public void run()
    {
        try {
            while (true) {
                selector.select(30L);
                Iterator selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        log.debug("colse");
                        getSocket().close();
                        setSocket(null);
                        setStatus(0);
                    }
                    if (key.isConnectable())
                        finishConnection(key);
                    else if (key.isReadable())
                        read(key);
                }
            }
        } catch (Exception e) {
            log.error("SocketClientLink run Exception", e);
        }
    }

    private void read(SelectionKey key) throws IOException
    {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        readBuffer.clear();
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
            log.debug(String.valueOf(linkId) + " " + numRead + " read key");
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            connectHandler.changeWaitFlag(false);
            setStatus(-2);
            log.error("SockectClientLink read message error,key:" + key, e);
            return;
        }
        if (numRead == -1) {
            key.channel().close();
            key.cancel();
            connectHandler.changeWaitFlag(false);
            setStatus(-1);
            log.debug("No message need read");
        } else
            handleResponse(socketChannel, readBuffer.array(), numRead);
    }

    private void handleResponse(SocketChannel socketChannel, byte[] data, int numRead) throws IOException {
        byte[] rspData = new byte[numRead];
        System.arraycopy(data, 0, rspData, 0, numRead);
        this.handler.Response(this.linkId, rspData);
    }

    private void finishConnection(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        try
        {
            try {
                socketChannel.finishConnect();
                setStatus(1);

                this.connectHandler.changeWaitFlag(false);
            } catch (ConnectException e) {
                this.log.error("Can't connet GWServer or Server is closed", e);
            }
        } catch (IOException e) {
            this.log.error("socketChannel is Exception,method:finishConnection", e);
            key.cancel();
            setStatus(0);
            return;
        }

        key.interestOps(1);
    }

    public void initiateConnection()
    {
        try{
            this.log.debug("准备初始化SocketChannel");
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            this.log.debug("准备连接到"+hostAddress+":"+port);
            socketChannel.connect(new InetSocketAddress(this.hostAddress, this.port));
            this.log.debug("注册selector");
            socketChannel.register(this.selector, 8);
            setSocket(socketChannel);
            this.log.debug("初始化SocketChannel完毕");
        }catch (Exception e){
            this.log.error("initiateConnection is fail,method:initiateConnection", e);
        }
    }

    private Selector initSelector() throws IOException {
        return Selector.open();
    }

    public synchronized int getStatus() {
        return this.status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public SocketChannel getSocket() {
        return this.socket;
    }

    public synchronized void setSocket(SocketChannel socket) {
        this.socket = socket;
    }

    public void login() {
        this.loginHandler.login(this.linkId);
        setStatus(2);
        this.connectHandler.changeWaitFlag(true);
        this.log.debug("login linkId:" + this.linkId);
    }

    public void loginSuccess() {
        setStatus(3);
        this.connectHandler.changeWaitFlag(false);
        this.log.debug("login success,linkid:" + this.linkId);
    }

    public void loginFail() {
        setStatus(-2);
        this.connectHandler.changeWaitFlag(false);
        this.log.debug("login falie, linkid:" + this.linkId);
    }
}