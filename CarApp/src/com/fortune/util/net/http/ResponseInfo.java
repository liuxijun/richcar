package com.fortune.util.net.http;

import org.apache.http.*;

import java.util.Locale;

/**
 * Author: wyouflf Date: 13-10-26 Time: 下午3:20
 */
public final class ResponseInfo<T> {
	public T result;
	public final boolean resultFormCache;

	public final Header[] allHeaders;
	public final Locale locale;

	// status line
	public final int statusCode;
	public final ProtocolVersion protocolVersion;
	public final String reasonPhrase;

	// entity
	public final long contentLength;
	public final Header contentType;
	public final Header contentEncoding;

	public ResponseInfo(final HttpResponse response, T result, boolean resultFormCache) {
		this.result = result;
		this.resultFormCache = resultFormCache;

		if (response != null) {
			allHeaders = response.getAllHeaders();
			locale = response.getLocale();

			// status line
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null) {
				statusCode = statusLine.getStatusCode();
				protocolVersion = statusLine.getProtocolVersion();
				reasonPhrase = statusLine.getReasonPhrase();
			}
			else {
				statusCode = 0;
				protocolVersion = null;
				reasonPhrase = null;
			}

			// entity
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				contentLength = entity.getContentLength();
				contentType = entity.getContentType();
				contentEncoding = entity.getContentEncoding();
			}
			else {
				contentLength = 0;
				contentType = null;
				contentEncoding = null;
			}
		}
		else {
			allHeaders = null;
			locale = null;

			// status line
			statusCode = 0;
			protocolVersion = null;
			reasonPhrase = null;

			// entity
			contentLength = 0;
			contentType = null;
			contentEncoding = null;
		}
	}
}
