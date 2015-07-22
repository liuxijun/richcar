package com.fortune.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
/**
 * Created by IntelliJ IDEA.
 * User: Alen
 * Date: 11-9-1
 * Time: ����3:59
 *
 */
public class ZipUtils {
    static final int BUFFER = 1024;
    /**
    * ��zip��ʽѹ���ļ�
    * @param zipFile ѹ������ļ��� ����·�� �磺"c:\\test.zip"
    * @param inputFile Ҫѹ�����ļ� �������ļ����ļ��� �磺"c:\\test" �� "c:\\test.doc"
    * @throws Exception
    *ant�µ�zip����Ĭ��ѹ������ΪUTF-8���룬��winRAR���ѹ�����õ�windowsĬ�ϵ�GBK����GB2312����
    *��antѹ����ŵ�windows�������������ļ������룬��winRARѹ�����ļ�����ant��ѹҲ��������룬
    *���ԣ�����ant����winRARѹ�����ļ�ʱ��Ҫ����ѹ������
    */
    public static void zip( File zipFile, String inputFile)
        throws Exception {
       File f = new File(inputFile);
       //File temp = new File(zipFileName);
       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
         zipFile));
//����ѹ������
       out.setEncoding("GBK");//����ΪGBK����windows�¾Ͳ��������ˣ����Ҫ�ŵ�Linux����Unix�¾Ͳ�Ҫ������
       zip(out, f, "");// �ݹ�ѹ������
       System.out.println("zip done");
       out.close();
    }
    private static void zip(ZipOutputStream out, File f, String base)
        throws Exception {
       System.out.println("Zipping   " + f.getName()); // ��¼��־����ʼѹ��
       if (f.isDirectory()) { // ������ļ��У����ȡ����������ļ�
        File[] fl = f.listFiles();
        out.putNextEntry(new ZipEntry(base + "/"));// �˴�Ҫ���ļ�д���ļ�����ֻ�����ļ���ǰ��"/"�ټ��ļ�����
        base = base.length() == 0 ? "" : base + "/";
        for (int i = 0; i < fl.length; i++) {
         zip(out, fl[i], base + fl[i].getName());
        }
       } else { // ������ļ�����ѹ��
        out.putNextEntry(new ZipEntry(base)); // ������һ��ѹ���ڵ�
        FileInputStream in = new FileInputStream(f); // ��ȡ�ļ�����
        int len;
        byte[] buf = new byte[BUFFER];
        while ((len = in.read(buf, 0, BUFFER)) != -1) {
         out.write(buf, 0, len); // д�뵽ѹ����
        }
        in.close();
       }
    }


    /**
    * ��ѹ��zip�ļ�
    * @param fileName Ҫ��ѹ���ļ��� ����·�� �磺"c:\\test.zip"
    * @param filePath ��ѹ�����ļ���·�� �磺"c:\\temp"
    * @throws Exception
    */
    public static void unZip(String fileName, String filePath) throws Exception{
       ZipFile zipFile = new ZipFile(fileName,"GBK"); //�ԡ�GBK�����봴��zip�ļ�����������winRARѹ�����ļ���
       Enumeration emu = zipFile.getEntries();


       while(emu.hasMoreElements()){
        ZipEntry entry = (ZipEntry) emu.nextElement();
        if (entry.isDirectory()){
         new File(filePath+entry.getName()).mkdirs();
         continue;
        }
        BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

        File file = new File(filePath + entry.getName());
        File parent = file.getParentFile();
        if(parent != null && (!parent.exists())){
         parent.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos,BUFFER);
        byte [] buf = new byte[BUFFER];
        int len = 0;
        while((len=bis.read(buf,0,BUFFER))!=-1){
         fos.write(buf,0,len);
        }
        bos.flush();
        bos.close();
        bis.close();
        System.out.println("��ѹ�ļ���"+file.getName());
       }
       zipFile.close();
    }


}
