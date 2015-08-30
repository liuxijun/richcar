package com.fortune.util;

import android.text.TextUtils;

public class URLUtil {
	/**
	 * 是否是有效的图片url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isValidateImgUrl(String url) {
		boolean isValidate = false;
		String[] suffixs = new String[] { ".jpg", ".jpeg", ".png", ".bmp", ".gif" };
		if (TextUtils.isEmpty(url)) {
			return isValidate;
		}
		if (!url.contains(".")) {
			return isValidate;
		}
		String suffix = url.substring(url.lastIndexOf("."));
		for (int i = 0; i < suffixs.length; i++) {
			if (suffixs[i].equalsIgnoreCase(suffix)) {
				isValidate = true;
				break;
			}
		}
		return isValidate;
	}
}
