package com.fortune.mobile.view.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

/*
 * author:zhangxi
 * 该类为drawable对象缓存类，解决首页推荐连续多次请求加载导致的oom异常问题。
 * 该缓存只存放kv图drawable对象。
 */
public class ImageCache {

	final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final int cacheSize = maxMemory / 4;
	private LruCache<String, Drawable> mMemeoryCache;
	private static ImageCache imageCache;

	private ImageCache() {
		if (mMemeoryCache == null) {
			mMemeoryCache = new LruCache<String, Drawable>(cacheSize);
		}
	};

	public static ImageCache getImageCache() {
		if (imageCache == null) {
			imageCache = new ImageCache();
		}
		return imageCache;
	}

	public void addDrawableToMemoryCache(String key, Drawable drawable) {
		if (getDrawableFromMemCache(key) == null) {
			mMemeoryCache.put(key, drawable);
		}
	}

	public Drawable getDrawableFromMemCache(String key) {
		return mMemeoryCache.get(key);
	}

}
