package com.fortune.mobile.download;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import com.fortune.util.Types;
import com.fortune.util.ULog;
import com.fortune.util.Util;

import java.io.Serializable;

/**
 * 下载文件的信息类
 * 
 * @author dongfang
 */
public class DownloadInfo implements Serializable {
	public final String TAG = DownloadInfo.class.getSimpleName();

	/**
	 * 
	 */
	private static final long serialVersionUID = 187654567898765456L;

	public static final String KEY_CONTENT_ID = "contentId";
	public static final String KEY_CONTENT_NAME = "contentName";
	public static final String KEY_CONTENT_TYPE = "contentType";
	public static final String KEY_CONTENT_URL = "contentUrl";
	public static final String KEY_CURRENT_BYTES = "currentBytes";
	public static final String KEY_TOTAL_BYTES = "totalbytes";
	public static final String KEY_FILE_NAME = "fileName";
	public static final String KEY_FILE_PATH = "filePath";
	public static final String KEY_ICON_URL = "iconUrl";
	public static final String KEY_ICON_NAME = "iconName";
	public static final String KEY_ICON_PATH = "iconPath";
	public static final String KEY_STATUS = "status";
	public static final String KEY_TIMESTAMP = "timeStamp";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_VIDEO_LENGTH = "videoLength";
	public static final String KEY_ERROR_CODE = "errorCode";

	/** 文件的唯一编号,视频文件必须非空 */
	public String contentId;
	/** 文件显示名称 : "Life_of_PI" */
	public String contentName;
	/** 文件类型，如视频，图片等 */
	public int contentType;
	/** 文件下载地址 */
	public String contentUrl;
	/** 文件 当前下载的字节数 */
	public long currentBytes;
	/** 文件总字节数 */
	public long totalbytes;
	/** 文件被保存的名称: "Life_of_PI.3pg" */
	public String fileName;
	/** 文件完整路径 ："sdcard/TYSX/dl/Life_of_PI.3pg" */
	public String filePath;
	/** 文件对应展示图片地址 */
	public String iconUrl;
	/** 文件对应展示名称: "1234.png" */
	public String iconName;
	/** 文件对应展示图片完整路径："data/data/xxx.xxx.xxx/cache/1234.jpe" */
	public String iconPath;
	/** 下载状态：如未下载，下载中，下载完成，取消下载等,默认为等待状态 */
	public int status;
	/** 最后一次下载操作时间 */
	public long timeStamp;
	/** 内容描述 */
	public String description;
	/** 视频播放时长 */
	public String videoLength;

	public DownloadInfo() {}

	/**
	 * 根据下载地址参数 contentUrl初始化下载信息
	 * 
	 * @param context
	 * @param contentUrl
	 */
	public DownloadInfo(Context context, String contentUrl) {
		this.contentUrl = contentUrl.trim();
		fileName = TextUtils.isEmpty(contentUrl) ? contentUrl : contentUrl.substring(contentUrl.lastIndexOf("/") + 1);
		contentName = fileName.substring(0, fileName.indexOf("."));
		contentType = initContentType(context, fileName);
		contentId = contentName;
		filePath = initFilePath(context, contentType);
	}

	/**
	 * 根据下载地址参数 contentUrl初始化用MyImageView控件的下载信息
	 * 
	 * @param context
	 * @param contentUrl
	 */
	public DownloadInfo(Context context, String contentUrl, boolean isUseImage) {
		this.contentUrl = contentUrl.trim();
		// url http://180.168.69.121:8089/files/open.jpg
		// 将filename 由 open.jpg改为%files%open.jpg
		if (TextUtils.isEmpty(contentUrl)) {
			return;
		}
		if (!contentUrl.contains("/")) {
			return;
		}
		if (contentUrl.contains("/") && contentUrl.split("/").length > 3) {
			String sub1 = contentUrl.substring(contentUrl.indexOf("/") + 1);
			String sub2 = sub1.substring(sub1.indexOf("/") + 1);
			String sub3 = sub2.substring(sub2.indexOf("/") + 1);
			fileName = contentUrl.substring(contentUrl.indexOf(sub3)).replace("/", "%");
			contentName = fileName.substring(0, fileName.indexOf("."));
			contentType = initContentType(context, fileName);
			filePath = initFilePath(context, contentType);
		}
	}

	public DownloadInfo(Context context, Bundle data) {
		init(context, data);
	}

	/** 初始化所有属性 */
	public void init(Context context, Bundle data) {
		contentId = data.getString(KEY_CONTENT_ID);
		contentName = data.getString(KEY_CONTENT_NAME);
		contentType = data.getInt(KEY_CONTENT_TYPE, -1);
		contentUrl = data.getString(KEY_CONTENT_URL);
		currentBytes = data.getLong(KEY_CURRENT_BYTES);
		totalbytes = data.getLong(KEY_TOTAL_BYTES);
		fileName = data.getString(KEY_FILE_NAME);
		filePath = data.getString(KEY_FILE_PATH);
		iconUrl = data.getString(KEY_ICON_URL);
		iconName = data.getString(KEY_ICON_NAME);
		iconPath = data.getString(KEY_ICON_PATH);
		status = data.getInt(KEY_STATUS, DownloadTask.STATE_WAITING);
		description = data.getString(KEY_DESCRIPTION);

		fileName = TextUtils.isEmpty(fileName) ? initFileName(context, contentUrl) : fileName;
		contentName = TextUtils.isEmpty(contentName) ? fileName.substring(0, fileName.indexOf(".")) : contentName;
		contentType = (contentType < 0) ? initContentType(context, fileName) : contentType;
		filePath = TextUtils.isEmpty(filePath) ? initFilePath(context, contentType) : filePath;
		description = TextUtils.isEmpty(description) ? fileName : description;

	};

	/** 初始化文件名称(fileName) */
	private String initFileName(Context context, String url) {
		String fileName_temp = "";
		if (url.contains("?"))
			fileName_temp = url.substring(contentUrl.lastIndexOf("/") + 1, contentUrl.indexOf("?"));
		else {
			fileName_temp = contentUrl.substring(contentUrl.lastIndexOf("/") + 1);
		}
		ULog.d(fileName_temp);
		if (TextUtils.isEmpty(contentName))
			return fileName_temp;
		return contentName + fileName_temp.substring(fileName_temp.lastIndexOf("."));
	}

	/** 初始化文件类型 */
	private int initContentType(Context context, String fileName) {
		int type = Types.DOWNLOAD_TYPE_OTHER;
		if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".gif")) {
			type = Types.DOWNLOAD_TYPE_IMAGE;
		}
		else if (fileName.endsWith(".3gp") || fileName.endsWith(".mp4")) {
			type = Types.DOWNLOAD_TYPE_VIDEO;
		}
		else if (fileName.endsWith(".mp3") || fileName.endsWith(".wav")) {
			type = Types.DOWNLOAD_TYPE_AUDIO;
		}
		else if (fileName.endsWith(".apk")) {
			type = Types.DOWNLOAD_TYPE_APP;
		}
		else {
			type = Types.DOWNLOAD_TYPE_OTHER;
		}

		return type;
	}

	/**
	 * 初始化文件保存完整路径 ,图片默认为sdcard/Android/data/com.xxx.xxx/xxxxx.png ,次默认为data/data/com.xxx.xxx/cache/xxxxx.png
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	private String initFilePath(Context context, int type) {
		if (Types.DOWNLOAD_TYPE_IMAGE == type) {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO && null != context.getExternalCacheDir()) {
				return context.getExternalCacheDir().getPath() + "/" + fileName;
			}
			else if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Util.getAvailableExternalMemorySize() > 5 * 1024 * 1024) {
				return Environment.getExternalStorageDirectory().getPath() + "/Android/data/"
						+ context.getPackageName() + "/cache/" + fileName;
			}
			else {
				return context.getCacheDir().getPath() + "/" + fileName;
			}
		}
		return Util.getDownloadPath(context) + "/" + fileName;
	}

	public DownloadInfo copy(DownloadInfo dlinfo) {
		this.contentId = dlinfo.contentId;
		this.contentName = dlinfo.contentName;
		this.contentType = dlinfo.contentType;
		this.contentUrl = dlinfo.contentUrl;
		this.currentBytes = dlinfo.currentBytes;
		this.totalbytes = dlinfo.totalbytes;
		this.fileName = dlinfo.fileName;
		this.filePath = dlinfo.filePath;
		this.iconName = dlinfo.iconName;
		this.iconPath = dlinfo.iconPath;
		this.iconUrl = dlinfo.iconUrl;
		this.status = dlinfo.status;
		this.timeStamp = dlinfo.timeStamp;
		this.description = dlinfo.description;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(KEY_CONTENT_ID).append(" = ").append(contentId).append("\n");
		sb.append(KEY_CONTENT_NAME).append(" = ").append(contentName).append("\n");
		sb.append(KEY_CONTENT_TYPE).append(" = ").append(contentType).append("\n");
		sb.append(KEY_CONTENT_URL).append(" = ").append(contentUrl).append("\n");
		sb.append(KEY_CURRENT_BYTES).append(" = ").append(currentBytes).append("\n");
		sb.append(KEY_TOTAL_BYTES).append(" = ").append(totalbytes).append("\n");
		sb.append(KEY_FILE_NAME).append(" = ").append(fileName).append("\n");
		sb.append(KEY_FILE_PATH).append(" = ").append(filePath).append("\n");
		sb.append(KEY_ICON_URL).append(" = ").append(iconUrl).append("\n");
		sb.append(KEY_ICON_NAME).append(" = ").append(iconName).append("\n");
		sb.append(KEY_ICON_PATH).append(" = ").append(iconPath).append("\n");
		sb.append(KEY_STATUS).append(" = ").append(status).append("\n");
		sb.append(KEY_DESCRIPTION).append(" = ").append(description).append("\n");
		sb.append(KEY_TIMESTAMP).append(" = ").append(timeStamp).append("\n");
		return sb.toString();
	}

}