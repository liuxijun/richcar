package com.fortune.mobile.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.*;
import android.os.storage.StorageManager;
import com.fortune.mobile.download.DownloadInfo;
import com.fortune.util.TVException;
import com.fortune.util.Types;
import com.fortune.util.ULog;
import com.fortune.util.Util;
import com.fortune.util.net.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class LoadImageRunnable implements Runnable {

	public static final String TAG = LoadImageRunnable.class.getSimpleName();

	DownloadInfo dlInfo;
	// OnDownloadListener listener;
	public static final int STATE_WAITING = 0; // 处于等待状态
	public static final int STATE_DOWNLOADING = 1; // 正在下载状态
	public static final int STATE_PAUSE = 2; // 处于暂停
	public static final int STATE_FINISH = 3; // 已经下载完成，重复下载时出现的提示
	public static final int STATE_CANCEL = 4; // 取消下载
	public static final int STATE_ERROR = 5; // 下载错误（无法下载，断网等）
	public static final int BUFFER_SIZE = 1024 * 2;
	private Context context;
	private Handler handler;

	public LoadImageRunnable(Context context, final DownloadInfo info, Handler handler) {
		this.context = context;
		this.dlInfo = info;
		this.handler = handler;
	}

	@SuppressLint("NewApi")
	@Override
	public void run() {
		try {
			Bundle bundle = new Bundle();
			/** 创建完成路径 */
			new File(dlInfo.filePath.substring(0, dlInfo.filePath.lastIndexOf("/"))).mkdirs();
			dlInfo.status = STATE_WAITING;
			dlInfo.timeStamp = System.currentTimeMillis();

			// 在每个任务下载之前，执行preDownload()，将需要下载的任务信息保存到数据库
			// if (null != listener)
			// listener.preDownload(dlInfo);
			Message msg = new Message();
			msg.what = 101;
			handler.sendMessage(msg);

			// 需要上报的参数列表
			File file = new File(dlInfo.filePath + "temp");
			dlInfo.currentBytes = file.length();
			List<NameValuePair> list = null;
			if (Types.DOWNLOAD_TYPE_IMAGE != dlInfo.contentType && 0 < dlInfo.currentBytes) {
				list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("Range", "bytes=" + dlInfo.currentBytes + "-"));
			}
			HttpURLConnection connect = Http.getInstance(context).getHttpURLConnection(dlInfo.contentUrl, null, list);

			// 断点续传时，流被seek了，getContentLength()获取的是未下载的大小而非总大小，所以不需要重复设置
			if (dlInfo.currentBytes <= 0)
				dlInfo.totalbytes = connect.getContentLength();

			boolean flag = false; // 是否有存储空间标识
			final long needSpace = dlInfo.totalbytes - dlInfo.currentBytes;
			long freeStorage = 0;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				if (dlInfo.filePath.contains("sdcard")) {
					freeStorage = Util.getAvailableExternalMemorySize();
				}
				else {
					freeStorage = Util.getAvailableInternalMemorySize();
				}
			}
			else {
				freeStorage = new File(dlInfo.filePath.substring(0, dlInfo.filePath.lastIndexOf("/"))).getFreeSpace();
			}

			if (needSpace > freeStorage) {
				ULog.i("Current downloadpath do not have enough free space ! " + needSpace + " -- " + freeStorage);
				try {
					StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
					// 获取sdcard的路径：外置和内置
					String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths", String.class)
							.invoke(sm, String.class);
					if (null != paths) {
						for (String path : paths) {
							// 非当前存储器
							if (!dlInfo.filePath.contains(path)) {
								long tempFreeStorage = new File(path).getFreeSpace();
								if (needSpace < tempFreeStorage) {
									if (Types.DOWNLOAD_TYPE_IMAGE == dlInfo.contentType)
										dlInfo.filePath = path + "/Android/data/" + context.getPackageName()
												+ "/cache/" + dlInfo.fileName;
									else
										dlInfo.filePath = path + "/TYSX/dl/" + dlInfo.fileName;
									// 断点续传，有新的存储空间，删除以前的未下载完成的temp文件
									if (dlInfo.currentBytes > 0)
										file.delete();
									file = null;
									file = new File(dlInfo.filePath + "temp");
									flag = true;
									Util.saveDownloadPath(context, path + "/TYSX/dl");
									ULog.i("DownloadPath change to " + dlInfo.filePath);
									break;
								}
							}
						}
					}
					if (!flag && !dlInfo.filePath.contains(Environment.getDataDirectory().getPath())
							&& Environment.getDataDirectory().getFreeSpace() > 50 * 1024 * 1024) {
						// 检测手机内存是否有存储空间
						long memoryFreeStorage = Environment.getDataDirectory().getFreeSpace();
						if (needSpace < memoryFreeStorage) {
							dlInfo.filePath = Environment.getDataDirectory().getAbsolutePath() + "/TYSX/dl/"
									+ dlInfo.fileName;
							// 断点续传，有新的存储空间，删除以前的未下载完成的temp文件
							if (dlInfo.currentBytes > 0)
								file.delete();
							file = null;
							file = new File(dlInfo.filePath + "temp");
							flag = true;
							ULog.i("DownloadPath change to " + dlInfo.filePath);
						}
					}
					// 断点续传时，需在新的路径重新下载
					if (flag) {
						dlInfo.currentBytes = 0;
						new File(dlInfo.filePath.substring(0, dlInfo.filePath.lastIndexOf("/"))).mkdirs();
					}

				} catch (IllegalArgumentException e) {
					ULog.e("IllegalArgumentException");
				} catch (IllegalAccessException e) {
					ULog.e("IllegalAccessException");
				} catch (InvocationTargetException e) {
					ULog.e("InvocationTargetException");
				} catch (NoSuchMethodException e) {
					ULog.e("NoSuchMethodException");
				}

			}
			else
				flag = true;

			if (!flag) {
				bundle.putInt("statuscode", TVException.DOWNLOAD_NO_STORAGE_SPACE);
			}

			write(file, connect.getInputStream(), bundle);

			// 下载完成重命名
			if (STATE_FINISH == dlInfo.status) {
				file.renameTo(new File(dlInfo.filePath));
			}

			if (bundle.containsKey("statuscode")) {
				dlInfo.status = STATE_PAUSE;
				// if (null != listener)
				// listener.errorDownload(dlInfo, new TVException(0));
				msg = new Message();
				msg.what = 102;
				handler.sendMessage(msg);

				if (bundle.getInt("statuscode") == TVException.DOWNLOAD_NO_STORAGE_SPACE)
					// new DialogFactory(context).showToast("当前存储空间不足！", Toast.LENGTH_SHORT);
					ULog.e(bundle.containsKey("msg") ? bundle.getString("msg") : "下载异常" + "("
							+ bundle.getInt("statuscode") + ")");
			}
			else {
				// if (null != listener)
				// listener.finishDownload(dlInfo);
				msg = new Message();
				msg.what = 100;
				msg.obj = dlInfo;
				handler.sendMessage(msg);
			}

		} catch (Exception e) {
			ULog.e(" Exception >> " + e.toString());
		}
	}

	/** 写入文件 */
	private void write(File file, InputStream ins, Bundle bundle) throws TVException {
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.seek(dlInfo.currentBytes);
			byte[] b = new byte[BUFFER_SIZE];
			dlInfo.status = STATE_DOWNLOADING;

			// for(int num=0 , i=0 ; (num = ins.read(b)) >0 ; i++){
			/*
			 * ((0 >= dlInfo.totalbytes) ? true : dlInfo.currentBytes <= dlInfo.totalbytes) 该条件包含：试图对获取不到资源长度的资源进行下载
			 */
			for (int num = 0, i = 0; ((num = ins.read(b)) > 0)
					&& ((0 >= dlInfo.totalbytes) ? true : dlInfo.currentBytes <= dlInfo.totalbytes); i++) {
				/*
				 * ULog.v( "while --> total = " + dlInfo.totalbytes + " , current = " + dlInfo.currentBytes + ", num = "
				 * + num + ", i = " + i);
				 */
				// if (isCancelled()) {
				// ins.close();
				// accessFile.close();
				// return;
				// }

				accessFile.write(b, 0, num);
				dlInfo.currentBytes += num;

				// if (50 < i) {
				// i = 0;
				// publishProgress(dlInfo);
				// }
			}
			// 对资源长度异常的处理
			if (dlInfo.currentBytes <= 0) {
				bundle.putInt("statuscode", TVException.DOWNLOAD_CONTENT_LENGTH_ZERO);
				return;
			}
			dlInfo.status = STATE_FINISH;
			// publishProgress(dlInfo);
			return;
		} catch (FileNotFoundException e) {
			ULog.e(e.toString());
			throw new TVException(TVException.DOWNLOAD_FILE_NOT_FOUND_EXCEPTION);
		} catch (IOException e) {
			throw new TVException(TVException.DOWNLOAD_WRITE_IO_EXCEPTION);
		} finally {
			try {
				if (null != ins) {
					ins.close();
				}
				if (null != accessFile) {
					accessFile.close();
				}
			} catch (Exception e) {
				ULog.e(e.getMessage());
			}
		}
	}

}
