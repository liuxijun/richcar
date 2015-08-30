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

package com.fortune.util;

import org.apache.http.protocol.HTTP;

/**
 * Created by wyouflf on 13-8-30.
 */
public class CharsetUtils {

	private CharsetUtils() {}

	public static String toCharset(final String str, final String charset, int judgeCharsetLength) {
		try {
			String oldCharset = getEncoding(str, judgeCharsetLength);
			return new String(str.getBytes(oldCharset), charset);
		} catch (Throwable ex) {
			ULog.w(ex);
			return str;
		}
	}

	public static String getEncoding(final String str, int judgeCharsetLength) {
		String encode = CharsetUtils.defaultEncodingCharset;
		for (String charset : supportCharset) {
			if (isCharset(str, charset, judgeCharsetLength)) {
				encode = charset;
				break;
			}
		}
		return encode;
	}

	public static boolean isCharset(final String str, final String charset, int judgeCharsetLength) {
		try {
			String temp = str.length() > judgeCharsetLength ? str.substring(0, judgeCharsetLength) : str;
			return temp.equals(new String(temp.getBytes(charset), charset));
		} catch (Throwable e) {
			return false;
		}
	}

	public static String defaultEncodingCharset = HTTP.DEFAULT_CONTENT_CHARSET;

	public static String[] supportCharset = new String[] { "ISO-8859-1",

	"GB2312", "GBK", "GB18030",

	"US-ASCII", "ASCII",

	"ISO-2022-KR",

	"ISO-8859-2",

	"ISO-2022-JP", "ISO-2022-JP-2",

	"UTF-8" };
}
