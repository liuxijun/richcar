package com.fortune.util;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2009-10-4
 * Time: 20:50:54
 * �ļ������еĻص��ӿ�
 */
public interface FileUtilsDispatcher {
    public int onFileFound(File file);
    public int onDirectoryFound(File dir);
}
