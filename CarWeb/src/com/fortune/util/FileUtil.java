package com.fortune.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {
	private static Log log = LogFactory.getLog(FileUtil.class);
	private static final int BUFFER_SIZE = 16 * 1024;

	public static File copy(File src, String dstPath, String fileName) {
		InputStream in = null;
		OutputStream out = null;

		try {
			File parent = new File(dstPath);

			if (!parent.isDirectory()) {
				parent.mkdir();
			}
			File dst = new File(parent, fileName);
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			return dst;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File writeContinue(String dstPath, String fileName, String info) {
		File parent = new File(dstPath);
		if (!parent.isDirectory()) {
			parent.mkdir();
		}
		FileWriter theFile = null;
		PrintWriter pwriter = null;
		try {
			File file = new File(parent, fileName);
			if (file.exists()) {
				System.out.println("文件存在");
				// return false;
			} else {
				System.out.println("文件不存在，正在创建...");
				if (file.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
					return null;
				}
			}
			theFile = new FileWriter(file);
			pwriter = new PrintWriter(theFile);
			pwriter.print(info.toString() + "\n\r");

			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (pwriter != null) {
				pwriter.close();
			}
			if (theFile != null) {
				try {
					theFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static File writeNew(String filePath, String fileName, String info) {
		File parent = new File(filePath);
		if (!parent.isDirectory()) {
			parent.mkdir();
		}
		OutputStreamWriter output = null;
		try {
			File file = new File(parent, fileName);
			if (file.exists()) {
				System.out.println("文件存在");
				return null;
			} else {
				System.out.println("文件不存在，正在创建...");
				if (file.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
					return null;
				}
			}
			output = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			output.write(info);

			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static File writeNew(String filePath, String info) {

		OutputStreamWriter output = null;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				System.out.println("文件存在");
				return null;
			} else {
				System.out.println("文件不存在，正在创建...");
				if (file.createNewFile()) {
					System.out.println("文件创建成功！");
				} else {
					System.out.println("文件创建失败！");
					return null;
				}
			}
			output = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			output.write(info);

			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String readFileInfo(String file) {
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(file);
		if (f.exists()) {
			System.out.println("文件存在");
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));

				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				System.out.println(sb);
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
			return null;
		}

	}
	
	public static long getFileSize(File file){
		if (file!=null&&file.isFile()){
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(file);
				long size = fi.available();
				return size;
			} catch (IOException e) {
				log.info("can not get the file size.");
				e.printStackTrace();
				return 0l;
			}finally{
				if (fi!=null){
					try {
						fi.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else
			return 0l;
	}


    public static void unZIP(String zipFileName, String outputDirectory){
        try {
            ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
            //获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
            //当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            //输入流读取完成；
            ZipEntry z = in.getNextEntry();
            while (z != null) {
                log.debug("unziping " + z.getName());

                //创建以zip包文件名为目录名的根目录
                File f = new File(outputDirectory);
                f.mkdir();
                if (z.isDirectory()) {
                    String name = z.getName();
                    name = name.substring(0, name.length() - 1);
                    log.debug("name " + name);
                    f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                    log.debug("mkdir " + outputDirectory + File.separator + name);
                } else {
                    f = new File(outputDirectory + File.separator + z.getName());
                    f.createNewFile();
                    FileOutputStream out = new FileOutputStream(f);
                    int b;
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                    out.close();
                }
                //读取下一个ZipEntry
                z = in.getNextEntry();
            }
            in.close();
        }
        catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }

}
