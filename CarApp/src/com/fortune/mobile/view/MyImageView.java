package com.fortune.mobile.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fortune.mobile.download.DownloadInfo;
import com.fortune.mobile.download.DownloadTask;
import com.fortune.mobile.download.OnDownloadListener;
import com.fortune.car.app.R;
import com.fortune.mobile.view.utils.ImageCache;
import com.fortune.mobile.view.utils.LoadImageRunnable;
import com.fortune.util.ULog;
import com.fortune.util.URLUtil;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * KV 图类
 * 
 * @author dongfang
 * 
 */
public class MyImageView extends RelativeLayout {
	public static final String TAG = MyImageView.class.getSimpleName();

	private Context context;
	private ImageView imageView;
    private TextView title;
	// private MyProgressBar myProgressBar;
	private static int count;
	private boolean mRequest = false;
	private ExecutorService executorService = Executors.newFixedThreadPool(10); // 固定五个线程来执行任务

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyImageView(Context context) {
		super(context);
		init(context);
	}

	public void init(Context context) {
		this.context = context;

		View convertView = LayoutInflater.from(context).inflate(R.layout.my_imageview, null);
		// myProgressBar = (MyProgressBar)
		// convertView.findViewById(R.id.imageview_myprogressbar);
		imageView = (ImageView) convertView.findViewById(R.id.imageview_show);
        title = (TextView) convertView.findViewById(R.id.textViewForImage);
        if(title!=null){
            title.setVisibility(GONE);
        }
		this.setGravity(Gravity.CENTER);
		addView(convertView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case 100:
				DownloadInfo dlInfo = (DownloadInfo) msg.obj;
				count--;
				// ULog.d( "url =  " + imgUrl + " thread count :" + count);
				// 防止错位，通过Tag找到view
				ImageView imageViewByTag = (ImageView) findViewWithTag(dlInfo.filePath);
				if (imageViewByTag != null) {
					Drawable defaultDraw = Drawable.createFromPath(dlInfo.filePath);
					if (defaultDraw != null) {
						imageViewByTag.setBackgroundDrawable(defaultDraw);
					}
					imageViewByTag.refreshDrawableState();
					imageViewByTag.getBackground().setCallback(null);
				}
				mRequest = false;

				break;
			case 101:
				count++;
				break;

			case 102:
				mRequest = false;
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * @param url
	 */
	public void setImage1(String url) {
		if (TextUtils.isEmpty(url) || !URLUtil.isValidateImgUrl(url))
			return;
		final String imgUrl = url;
		if (!url.startsWith("http://"))
			return;
		DownloadInfo dlinfo = new DownloadInfo(context, url, true);

		// 防止错位，加Tag
		imageView.setTag(dlinfo.filePath);
		ImageView imageViewByTag = (ImageView) findViewWithTag(dlinfo.filePath);

		if (new File(dlinfo.filePath).exists()) {
			Drawable defaultDraw = null;
			// 缓存kv图drawable对象，解决oom异常
			if (ImageCache.getImageCache().getDrawableFromMemCache(url) != null) {
				defaultDraw = ImageCache.getImageCache().getDrawableFromMemCache(url);
			}
			else {
				defaultDraw = Drawable.createFromPath(dlinfo.filePath);
				if (defaultDraw != null) {
					ImageCache.getImageCache().addDrawableToMemoryCache(url, defaultDraw);
				}
			}
			if (null != defaultDraw) {
				if (imageViewByTag != null) {
					imageViewByTag.setBackgroundDrawable(defaultDraw);
				}
				// myProgressBar.setVisibility(View.GONE);
			}
			else {
				new File(dlinfo.filePath).delete(); // 删除下载好的无法使用的图片
			}
		}
		else {
			if (imageViewByTag != null) {
				imageViewByTag.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.image_bg));
			}
			ULog.d("reuest url :" + imgUrl + " send status :" + mRequest);
			if (mRequest) {
				return;
			}
			mRequest = true;

			// 下载图片
			new DownloadTask(context, dlinfo, new OnDownloadListener() {
				@Override
				public void updateProcess(DownloadInfo dlInfo) {}

				@Override
				public void preDownload(DownloadInfo dlInfo) {
					count++;
				}

				@Override
				public void finishDownload(DownloadInfo dlInfo) {
					// myProgressBar.setVisibility(View.GONE);
					count--;
					ULog.d("url =  " + imgUrl + " thread count :" + count);
					// 防止错位，通过Tag找到view
					ImageView imageViewByTag = (ImageView) findViewWithTag(dlInfo.filePath);
					if (imageViewByTag != null) {
						Drawable defaultDraw = Drawable.createFromPath(dlInfo.filePath);
						if (defaultDraw != null) {
							imageViewByTag.setBackgroundDrawable(defaultDraw);
						}
						imageViewByTag.refreshDrawableState();
						imageViewByTag.getBackground().setCallback(null);
					}
					mRequest = false;
				}

				@Override
				public void errorDownload(DownloadInfo dlInfo, Exception e) {
					mRequest = false;
				}
			}).execute();

		}

		// if (new File(dlinfo.filePath).exists()) {
		// Drawable defaultDraw = Drawable.createFromPath(dlinfo.filePath);
		// if (null != defaultDraw) {
		// try {
		// imageView.setBackgroundDrawable(defaultDraw);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// ULog.e( e.getMessage());
		// }
		// // myProgressBar.setVisibility(View.GONE);
		// }else{
		// new File(dlinfo.filePath).delete(); //删除下载好的无法使用的图片
		// }
		// }
		// else {
		// // 下载图片
		// new DownloadTask(context, dlinfo, new OnDownloadListener() {
		// @Override
		// public void updateProcess(DownloadInfo dlInfo) {}
		//
		// @Override
		// public void preDownload(DownloadInfo dlInfo) {}
		//
		// @Override
		// public void finishDownload(DownloadInfo dlInfo) {
		// // myProgressBar.setVisibility(View.GONE);
		// Drawable defaultDraw = Drawable.createFromPath(dlInfo.filePath);
		// if (null != defaultDraw) {
		// try {
		// imageView.setBackgroundDrawable(defaultDraw);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// ULog.e( e.getMessage());
		// }
		// }
		// // myProgressBar.setVisibility(View.GONE);
		// imageView.refreshDrawableState();
		// imageView.getBackground().setCallback(null);
		// }
		//
		// @Override
		// public void errorDownload(DownloadInfo dlInfo, TVException e) {}
		// }).execute();
		// }
	}

	public void setImage1(String url, boolean isCache) {
		if (TextUtils.isEmpty(url) || !URLUtil.isValidateImgUrl(url))
			return;
		final String imgUrl = url;
		if (!url.startsWith("http://"))
			return;;
		DownloadInfo dlinfo = new DownloadInfo(context, url, true);

		// 防止错位，加Tag
		imageView.setTag(dlinfo.filePath);
		ImageView imageViewByTag = (ImageView) findViewWithTag(dlinfo.filePath);

		if (new File(dlinfo.filePath).exists()) {
			Drawable defaultDraw = null;
			// 缓存kv图drawable对象，解决oom异常
			if (ImageCache.getImageCache().getDrawableFromMemCache(url) != null && isCache) {
				defaultDraw = ImageCache.getImageCache().getDrawableFromMemCache(url);
			}
			else {
				defaultDraw = Drawable.createFromPath(dlinfo.filePath);
				if (defaultDraw != null) {
					ImageCache.getImageCache().addDrawableToMemoryCache(url, defaultDraw);
				}
			}
			if (null != defaultDraw) {
				if (imageViewByTag != null) {
					imageViewByTag.setBackgroundDrawable(defaultDraw);
				}
				// myProgressBar.setVisibility(View.GONE);
			}
			else {
				new File(dlinfo.filePath).delete(); // 删除下载好的无法使用的图片
			}
		}
		else {
			if (imageViewByTag != null) {
				imageViewByTag.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.image_bg));
			}
			ULog.d("reuest url :" + imgUrl + " send status :" + mRequest);
			if (mRequest) {
				return;
			}
			mRequest = true;

			// 下载图片
			new DownloadTask(context, dlinfo, new OnDownloadListener() {
				@Override
				public void updateProcess(DownloadInfo dlInfo) {}

				@Override
				public void preDownload(DownloadInfo dlInfo) {
					count++;
				}

				@Override
				public void finishDownload(DownloadInfo dlInfo) {
					// myProgressBar.setVisibility(View.GONE);
					count--;
					ULog.d("url =  " + imgUrl + " thread count :" + count);
					// 防止错位，通过Tag找到view
					ImageView imageViewByTag = (ImageView) findViewWithTag(dlInfo.filePath);
					if (imageViewByTag != null) {
						Drawable defaultDraw = Drawable.createFromPath(dlInfo.filePath);
						if (defaultDraw != null) {
							imageViewByTag.setBackgroundDrawable(defaultDraw);
						}
						imageViewByTag.refreshDrawableState();
						imageViewByTag.getBackground().setCallback(null);
					}
					mRequest = false;
				}

				@Override
				public void errorDownload(DownloadInfo dlInfo, Exception e) {
					mRequest = false;
				}
			}).execute();
		}

	}
    public void setImage(String url,boolean isCache) {
        setImage(url,null,isCache);
    }
	public void setImage(String url,String caption, boolean isCache) {
		if (TextUtils.isEmpty(url) || !URLUtil.isValidateImgUrl(url))
			return;
        int p=url.indexOf("?");
        if(p>0){
            url = url.substring(0,p);
        }
        if(title!=null){
            if(caption!=null&&!"".equals(caption.trim())){
                title.setVisibility(View.VISIBLE);
                title.setText(caption);
            }else{
                title.setVisibility(View.GONE);
            }
        }
		final String imgUrl = url;
		if (!url.startsWith("http://"))
			return;
		DownloadInfo dlinfo = new DownloadInfo(context, url, true);

		// 防止错位，加Tag
		imageView.setTag(dlinfo.filePath);
		ImageView imageViewByTag = (ImageView) findViewWithTag(dlinfo.filePath);

		if (new File(dlinfo.filePath).exists()) {
			Drawable defaultDraw = null;
			// 缓存kv图drawable对象，解决oom异常
			if (ImageCache.getImageCache().getDrawableFromMemCache(url) != null && isCache) {
				defaultDraw = ImageCache.getImageCache().getDrawableFromMemCache(url);
			}
			else {
				defaultDraw = Drawable.createFromPath(dlinfo.filePath);
				if (defaultDraw != null) {
					ImageCache.getImageCache().addDrawableToMemoryCache(url, defaultDraw);
				}
			}
			if (null != defaultDraw) {
				if (imageViewByTag != null) {
					imageViewByTag.setBackgroundDrawable(defaultDraw);
				}
				// myProgressBar.setVisibility(View.GONE);
			}
			else {
				new File(dlinfo.filePath).delete(); // 删除下载好的无法使用的图片
			}
		}
		else {
			if (imageViewByTag != null) {
				imageViewByTag.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.image_bg));

			}
			ULog.d("reuest url :" + imgUrl + " send status :" + mRequest);
			if (mRequest) {
				return;
			}
			mRequest = true;
			executorService.submit(new LoadImageRunnable(context, dlinfo, mHandler));
		}

	}

	public void setImage(String url) {

		if (TextUtils.isEmpty(url) || !URLUtil.isValidateImgUrl(url)) {
			setBackgroundResource(R.drawable.image_bg);
			return;
		}
		final String imgUrl = url;
		if (!url.startsWith("http://"))
			return;
		DownloadInfo dlinfo = new DownloadInfo(context, url, true);

		// 防止错位，加Tag
		imageView.setTag(dlinfo.filePath);
		ImageView imageViewByTag = (ImageView) findViewWithTag(dlinfo.filePath);

		if (new File(dlinfo.filePath).exists()) {
			Drawable defaultDraw = null;
			// 缓存kv图drawable对象，解决oom异常
			if (ImageCache.getImageCache().getDrawableFromMemCache(url) != null) {
				defaultDraw = ImageCache.getImageCache().getDrawableFromMemCache(url);
			}
			else {
				defaultDraw = Drawable.createFromPath(dlinfo.filePath);
				if (defaultDraw != null) {
					ImageCache.getImageCache().addDrawableToMemoryCache(url, defaultDraw);
				}
			}
			if (null != defaultDraw) {
				if (imageViewByTag != null) {
					imageViewByTag.setBackgroundDrawable(defaultDraw);
				}
				// myProgressBar.setVisibility(View.GONE);
			}
			else {
				new File(dlinfo.filePath).delete(); // 删除下载好的无法使用的图片
			}
		}
		else {
			if (imageViewByTag != null) {
				imageViewByTag.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.image_bg));
			}
			ULog.d("reuest url :" + imgUrl + " send status :" + mRequest);
			if (mRequest) {
				return;
			}
			mRequest = true;

			executorService.submit(new LoadImageRunnable(context, dlinfo, mHandler));

		}

	}

	public void setImageDrawable(Drawable drawable) {
		imageView.setBackgroundDrawable(drawable);
	}

	public void setBackgroundResource(int resid) {
        if(imageView!=null){
            imageView.setBackgroundResource(resid);
        }else{
            Log.e(TAG,"系统组件是null，无法进行后续的操作！");
        }
	}

	public void setImageResource(int resId) {
		imageView.setImageResource(resId);
	}

	public interface ImageCallback {
		public void imageLoaded(DownloadInfo dlInfo);
	}

    public void setBorder(Color color,int borderWidth){
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        setLayoutParams(lp);
    }
}