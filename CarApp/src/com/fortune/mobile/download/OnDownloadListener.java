package com.fortune.mobile.download;

public interface OnDownloadListener {

	public void updateProcess(DownloadInfo dlInfo);

	public void finishDownload(DownloadInfo dlInfo);

	public void preDownload(DownloadInfo dlInfo);

	public void errorDownload(DownloadInfo dlInfo, Exception e);
}
