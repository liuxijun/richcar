package com.fortune.util.net;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2010-2-21
 * Time: 13:18:54
 * ���Ӻ�Ķ�ȡ����
 */
public interface ProtocolHandler {
    public void onConnected(InputStream inputStream, OutputStream outputStream);
}
