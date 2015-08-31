package com.fortune.mobile.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import com.fortune.mobile.view.imagegallery.ImageGalleyAdapter1;

import java.util.List;

/** kv图片滑动 @author dongfang */
public class ImageGallery extends LinearLayout {
	public static final String TAG = ImageGallery.class.getSimpleName();

	/** 一次显示一张图片 */
	public static final int IMAGE_VIEW_TYPE_1 = 1;
	/** 一次显示二张图片 */
	public static final int IMAGE_VIEW_TYPE_2 = 2;
	/** 一次显示三张图片 */
	public static final int IMAGE_VIEW_TYPE_3 = 3;

	private ViewPager viewPager;
	private LinearLayout ll_fling_desc_image;
	private TextView tv_fling_desc;

	//private List<HomeSliderItem> list;

	private Context context;
	/** 需要显示的图片的类型 */
	private int imageViewType = -1;

	public ImageGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
//		init();
	}
/*

	public ImageGallery(Context context, List<HomeSliderItem> list, int imageViewType) {
		super(context);
		this.imageViewType = imageViewType;
		this.context = context;
		init();
		setList();
	}

	public void init() {
		View view = LayoutInflater.from(context).inflate(R.layout.image_gallery, null);
		viewPager = (ViewPager) view.findViewById(R.id.imageGallery_viewpage);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, (int) (Util.getWindowWidth(context) * 270 / 640));
		viewPager.setLayoutParams(layoutParams);
		ll_fling_desc_image = (LinearLayout) view.findViewById(R.id.imageGallery_ll_fling_desc_image);
		tv_fling_desc = (TextView) view.findViewById(R.id.imageGallery_tv_fling_desc);
		addView(view);
		// viewPager.setCurrentItem(1);
	}

	public void setList(int imageViewType, List<HomeSliderItem> list) {
		this.imageViewType = imageViewType;
		this.list = list;
		setList();
	}

	public void setList() {
		if (null == list || null == list.get(0))
			return;
		int num = 1;
		if (IMAGE_VIEW_TYPE_2 == imageViewType)
			num = 2;
		else if (IMAGE_VIEW_TYPE_3 == imageViewType)
			num = 3;

		final int size = list.size() / num;

		if (size > 0) {
			ll_fling_desc_image.removeAllViews();
			tv_fling_desc.setText(list.get(0).getMEDIA_NAME());
			if (size < 2) {
				ll_fling_desc_image.setVisibility(View.GONE);
			}
			else {
				ll_fling_desc_image.setVisibility(View.VISIBLE);

				for (int i = 0; i < size; i++) {
					// ULog.i( "--- " + list.get(i).toString());
					ImageView image = new ImageView(context);
					LayoutParams params = new LayoutParams(-2, -2);
					params.setMargins(3, 0, 3, 0);
					ll_fling_desc_image.addView(image, params);
					if (i == 0) {
						image.setBackgroundResource(R.drawable.image_gralley_dot01);
					}
					else {
						image.setBackgroundResource(R.drawable.image_gralley_dot02);
					}
				}
			}

			if (IMAGE_VIEW_TYPE_1 == imageViewType) {
				// list.add(list.get(list.size() -1));
				// list.add(0, list.get(0));
				viewPager.setAdapter(new ImageGalleyAdapter1(context, list));
				// }
				viewPager.setCurrentItem(list.size() * 100);
			}
			// else if (IMAGE_VIEW_TYPE_2 == imageViewType) {
			// viewPager.setAdapter(new ImageGalleyAdapter2(context, list));
			// }
			// else if (IMAGE_VIEW_TYPE_3 == imageViewType) {
			// viewPager.setAdapter(new ImageGalleyAdapter3(context, list));
			// }

			viewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					ULog.d("onPageSelected arg0 = " + arg0);
					changeDesc(arg0 % list.size());
					// if (arg0 == 0) {
					// viewPager.setCurrentItem(list.size() - 1, false);
					// }
					// else

					// if (arg0 == list.size() - 1) {
					// viewPager.setCurrentItem(0, false);
					// }
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// if (listener != null) {
					// listener.OnPageScrolled();
					// }
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {}
			});
		}
	}

	public void changeDesc(int position) {
		for (int i = 0, length = ll_fling_desc_image.getChildCount(); i < length; i++) {
			ll_fling_desc_image.getChildAt(i).setBackgroundResource(R.drawable.image_gralley_dot02);
		}
		ll_fling_desc_image.getChildAt(position).setBackgroundResource(R.drawable.image_gralley_dot01);
		tv_fling_desc.setText(list.get(position).getMEDIA_NAME());
	}

	public int getCurrentIndex() {
		return viewPager.getCurrentItem();
	}
	*/

}