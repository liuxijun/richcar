/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortune.util.net.http;

import android.os.SystemClock;
import android.text.TextUtils;
import com.fortune.mobile.params.ComParams;
import com.fortune.util.HttpException;
import com.fortune.util.OtherUtils;
import com.fortune.util.ULog;
import com.fortune.util.User;
import com.fortune.util.core.CompatibleAsyncTask;
import com.fortune.util.net.http.client.HttpGetCache;
import com.fortune.util.net.http.client.HttpRequest;
import com.fortune.util.net.http.client.HttpRequest.HttpMethod;
import com.fortune.util.net.http.client.callback.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.UUID;

public class HttpHandler<T> extends CompatibleAsyncTask<Object, Object, Void> implements RequestCallBackHandler {

	private final AbstractHttpClient client;
	private final HttpContext context;

	private final StringDownloadHandler mStringDownloadHandler = new StringDownloadHandler();
	private final FileDownloadHandler mFileDownloadHandler = new FileDownloadHandler();

	private HttpRedirectHandler httpRedirectHandler;

	public void setHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
		if (httpRedirectHandler != null) {
			this.httpRedirectHandler = httpRedirectHandler;
		}
	}

	private String requestUrl;
	private HttpRequestBase request;
	private boolean isUploading = true;
	private final RequestCallBack<T> callback;

	private int retriedTimes = 0;
	private String fileSavePath = null;
	private boolean isDownloadingFile = false;
	private boolean autoResume = false; // Whether the downloading could continue from the point of interruption.
	private boolean autoRename = false; // Whether rename the file by response header info when the download completely.
	private String charset; // The default charset of response header info.

	public HttpHandler(AbstractHttpClient client, HttpContext context, String charset, RequestCallBack<T> callback) {
		this.client = client;
		this.context = context;
		this.callback = callback;
		this.charset = charset;
	}

	private long expiry = HttpGetCache.getDefaultExpiryTime();

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	// 执行请求
	@SuppressWarnings("unchecked")
	private ResponseInfo<T> sendRequest(HttpRequestBase request) throws HttpException {
		if (autoResume && isDownloadingFile) {
			File downloadFile = new File(fileSavePath);
			long fileLen = 0;
			if (downloadFile.isFile() && downloadFile.exists()) {
				fileLen = downloadFile.length();
			}
			if (fileLen > 0) {
				request.setHeader("RANGE", "bytes=" + fileLen + "-");
			}
		}

		boolean retry = true;
		HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
		while (retry) {
			IOException exception = null;
			try {
				// 不进行缓存操作
				// if (request.getMethod().equals(HttpRequest.HttpMethod.GET.toString())) {
				// String result = HttpUtils.sHttpGetCache.get(requestUrl);
				// if (result != null) {
				// return new ResponseInfo<T>(null, (T) result, true);
				// }
				// }

				ResponseInfo<T> responseInfo = null;
				if (!isCancelled()) {
					HttpResponse response = client.execute(request, context);
					responseInfo = handleResponse(response);
				}
				return responseInfo;
			} catch (UnknownHostException e) {
				exception = e;
				retry = retryHandler.retryRequest(exception, ++retriedTimes, context);
			} catch (IOException e) {
				exception = e;
				retry = retryHandler.retryRequest(exception, ++retriedTimes, context);
			} catch (NullPointerException e) {
				exception = new IOException(e.getMessage());
				exception.initCause(e);
				retry = retryHandler.retryRequest(exception, ++retriedTimes, context);
			} catch (HttpException e) {
				throw e;
			} catch (Throwable e) {
				exception = new IOException(e.getMessage());
				exception.initCause(e);
				retry = retryHandler.retryRequest(exception, ++retriedTimes, context);
			}
			if (!retry && exception != null) {
				throw new HttpException(exception);
			}
		}
		return null;
	}

	@Override
	protected Void doInBackground(Object... params) {
		if (params == null || params.length < 1)
			return null;

		if (params.length > 3) {
			fileSavePath = String.valueOf(params[1]);
			isDownloadingFile = fileSavePath != null;
			autoResume = (Boolean) params[2];
			autoRename = (Boolean) params[3];
		}
		try {
			// init request & requestUrl
			request = (HttpRequestBase) params[0];
			requestUrl = request.getURI().toString();
			if (callback != null) {
				callback.setRequestUrl(requestUrl);
			}
			this.publishProgress(UPDATE_START);
			lastUpdateTime = SystemClock.uptimeMillis();
			ResponseInfo<T> responseInfo = sendRequest(request);
			if (responseInfo != null) {
				this.publishProgress(UPDATE_SUCCESS, responseInfo);
				return null;
			}
		} catch (HttpException e) {
			this.publishProgress(UPDATE_FAILURE, e, e.getMessage());
		}

		return null;
	}

	private final static int UPDATE_START = 1;
	private final static int UPDATE_LOADING = 2;
	private final static int UPDATE_FAILURE = 3;
	private final static int UPDATE_SUCCESS = 4;

	@Override
	@SuppressWarnings("unchecked")
	protected void onProgressUpdate(Object... values) {
		if (mStopped || values == null || values.length < 1 || callback == null)
			return;
		switch ((Integer) values[0]) {
		case UPDATE_START:
			callback.onStart();
			break;
		case UPDATE_LOADING:
			if (values.length != 3)
				return;
			callback.onLoading(Long.valueOf(String.valueOf(values[1])), Long.valueOf(String.valueOf(values[2])),
					isUploading);
			break;
		case UPDATE_FAILURE:
			if (values.length != 3)
				return;
			callback.onFailure((HttpException) values[1], (String) values[2]);
			break;
		case UPDATE_SUCCESS:
			if (values.length != 2)
				return;
			callback.onSuccess((ResponseInfo<T>) values[1]);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private ResponseInfo<T> handleResponse(HttpResponse response) throws HttpException, IOException {
		if (response == null) {
			throw new HttpException("response is null");
		}
		if (isCancelled())
			return null;

		StatusLine status = response.getStatusLine();
		int statusCode = status.getStatusCode();
		if (statusCode < 300) {
			Object result = null;
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				isUploading = false;
				if (isDownloadingFile) {
					autoResume = autoResume && OtherUtils.isSupportRange(response);
					String responseFileName = autoRename ? OtherUtils.getFileNameFromHttpResponse(response) : null;
					result = mFileDownloadHandler
							.handleEntity(entity, this, fileSavePath, autoResume, responseFileName);
				}
				else {

					// Set charset from response header info if it's exist.
					String responseCharset = OtherUtils.getCharsetFromHttpResponse(response);
					charset = TextUtils.isEmpty(responseCharset) ? charset : responseCharset;

					result = mStringDownloadHandler.handleEntity(entity, this, charset);
					ULog.d(result.toString());
					// -------- token 失效时，根据ip重新获取一次token ,by dongfang, 2013-10-16 17:38:40----------------------
					if (isNewToken(result.toString())) {
						String newUrl = requestUrl.replace(User.SHAREDPREFERENCES_ACCESS_TOKEN_OLD,
								User.SHAREDPREFERENCES_ACCESS_TOKEN_NEW);
						request.setURI(URI.create(newUrl));
						return handleResponse(client.execute(request, context));
					}
					// ---------------------------------------------------------------------------------------------------
					// 不缓存数据 by dongfang, 2013-10-16 17:38:40
					// HttpUtils.sHttpGetCache.put(_getRequestUrl, (String) responseBody, expiry);
					// ---------------------------------------------------------------------------------------------------
				}
			}
			return new ResponseInfo<T>(response, (T) result, false);
		}
		else if (statusCode == 301 || statusCode == 302) {
			if (httpRedirectHandler == null) {
				httpRedirectHandler = new DefaultHttpRedirectHandler();
			}
			HttpRequestBase request = httpRedirectHandler.getDirectRequest(response);
			if (request != null) {
				return this.sendRequest(request);
			}
		}
		else if (statusCode == 416) {
			throw new HttpException(statusCode, "maybe the file has downloaded completely");
		}
		else {
			throw new HttpException(statusCode, status.getReasonPhrase());
		}
		return null;
	}

	private boolean mStopped = false;

	/**
	 * stop request task.
	 */
	@Override
	public void stop() {
		this.mStopped = true;
		if (!request.isAborted()) {
			try {
				request.abort();
			} catch (Throwable e) {}
		}
		if (!this.isCancelled()) {
			try {
				this.cancel(true);
			} catch (Throwable e) {}
		}
		if (callback != null) {
			callback.onStopped();
		}
	}

	@Override
	public boolean isStopped() {
		return mStopped;
	}

	private long lastUpdateTime;

	@Override
	public boolean updateProgress(long total, long current, boolean forceUpdateUI) {
		if (callback != null && !mStopped) {
			if (forceUpdateUI) {
				this.publishProgress(UPDATE_LOADING, total, current);
			}
			else {
				long currTime = SystemClock.uptimeMillis();
				if (currTime - lastUpdateTime >= callback.getRate()) {
					lastUpdateTime = currTime;
					this.publishProgress(UPDATE_LOADING, total, current);
				}
			}
		}
		return !mStopped;
	}

	/** token过期重新获取token */

	private boolean isNewToken(String result) {
/*
		try {
			LoginBean bean = new com.google.gson.Gson().fromJson(result, LoginBean.class);
			// ULog.d(bean.toString());
			if (!bean.isResult() && !bean.isSuccess()) {
				String url_token = ComParams.HTTP_GET_TOKEN_BY_UUID + UUID.randomUUID().toString().replace("-", "");
				HttpRequest request = new HttpRequest(HttpMethod.GET, url_token);

				HttpResponse response = client.execute(request, context);
				String responseBody = handleResponse(response).result.toString();
				ULog.d(responseBody);

				LoginBean newbean = new com.google.gson.Gson().fromJson(responseBody, LoginBean.class);

				if (null != newbean && newbean.isSuccess() && !TextUtils.isEmpty(newbean.getToken())) {
					// 保存token
					User.SHAREDPREFERENCES_ACCESS_TOKEN_NEW = newbean.getToken();
					return true;
				}
			}
		} catch (Throwable t) {
			// t.printStackTrace();
		}
*/
		return false;
	}

}
