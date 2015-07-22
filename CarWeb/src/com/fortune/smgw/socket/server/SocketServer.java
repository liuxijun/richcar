package com.fortune.smgw.socket.server;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 13-3-16
 * Time: ÉÏÎç8:44
 * To change this template use File | Settings | File Templates.
 */
import com.fortune.smgw.socket.iinterface.IReqHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SocketServer
        implements Runnable
{
    private Logger log = Logger.getLogger(SocketServer.class);
    private InetAddress hostAddress;
    private int port;
    private Thread serverThread;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    private IReqHandler[] handler;
    private int curHandlerId;
    private int threadNum = 20;

    private List channelList = new ArrayList();

    private List pendingChanges = new LinkedList();

    private Map pendingData = new HashMap();

    public SocketServer(InetAddress hostAddress, int port, Class handlerClass) throws IOException {
        this.hostAddress = hostAddress;
        this.port = port;
        this.selector = initSelector();
        try
        {
            this.handler = new IReqHandler[this.threadNum];
            for (int i = 0; i < this.threadNum; i++)
                this.handler[i] = ((IReqHandler)handlerClass.newInstance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.serverThread = new Thread(this);
        this.serverThread.start();
    }

    public void send(SocketChannel socket, byte[] data)
    {
        this.log.debug("send to:" + socket.socket().getRemoteSocketAddress().toString() + " " + data.length + " byte data.");
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            buffer.flip();
            socket.write(ByteBuffer.wrap(data));
        }
        catch (Exception e) {
            this.log.error("", e);
        }
    }

    public void run() {
        for (;;) {
            try {
                synchronized (pendingChanges) {
                    Iterator changes = pendingChanges.iterator();
                    while (changes.hasNext()) {
                        ChangeStatus change = (ChangeStatus) changes.next();
                        switch (change.type) {
                            case 2: {
                                SelectionKey key = change.socket.keyFor(selector);
                                key.interestOps(change.ops);
                                break;
                            }
                        }
                    }
                    pendingChanges.clear();
                }
                selector.select();
                Iterator selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();
                    if (key.isValid()) {
                        if (key.isAcceptable())
                            accept(key);
                        else if (key.isReadable())
                            read(key);
                    }
                }
            } catch (Exception e) {
                log.error("SocketServer run exception:", e);
            }
        }

    }

    private void accept(SelectionKey key)
            throws IOException
    {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();

        SocketChannel socketChannel = serverSocketChannel.accept();

        socketChannel.configureBlocking(false);

        socketChannel.register(this.selector, 1);

        this.channelList.add(socketChannel);
        this.log.info(socketChannel.socket().getRemoteSocketAddress().toString() + " connect success!");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        log.debug(socketChannel.socket().getRemoteSocketAddress().toString()
                + " begin to read byte array");
        readBuffer.clear();
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
            log.debug("read " + socketChannel.socket().getRemoteSocketAddress
                    ().toString() + " " + numRead + " byte ");
        } catch (IOException e) {
            channelList.remove(socketChannel);
            key.cancel();
            socketChannel.close();
            log.error(socketChannel.socket().getRemoteSocketAddress()
                    .toString(),
                    e);
            return;
        }
        if (numRead == -1) {
            channelList.remove(socketChannel);
            key.channel().close();
            key.cancel();
            log.error("this connect is alreay close");
        } else
            handleRequest(socketChannel, readBuffer.array(), numRead);
    }

    private void handleRequest(SocketChannel socketChannel, byte[] data, int numRead) throws IOException {
        byte[] rspData = new byte[numRead];
        System.arraycopy(data, 0, rspData, 0, numRead);

        this.curHandlerId %= this.threadNum;
        int handlerId = this.curHandlerId;
        this.curHandlerId += 1;
        this.handler[handlerId].Request(this, socketChannel, rspData);
    }

    private Selector initSelector()
            throws IOException
    {
        Selector socketSelector = SelectorProvider.provider().openSelector();

        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);

        InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
        this.serverChannel.socket().bind(isa);

        this.serverChannel.register(socketSelector, 16);
        this.log.info(this.hostAddress.getHostAddress() + ":" + this.port + " is begin to listen!");
        return socketSelector;
    }
}