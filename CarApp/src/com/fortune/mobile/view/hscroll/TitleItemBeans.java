package com.fortune.mobile.view.hscroll;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 导航栏item类
 * 
 * type说明：0为显示可变更，1为不显示可变更，2为显示不可变更，3为"更多"
 * 
 * @author chen
 * 
 */
public class TitleItemBeans implements Parcelable {
	private String contentId;
	private String productId;
	private int clickType;
	private String clickParam;
	private String name;
	private int sortId;
	private int type;

	/**
	 * @return the clickType
	 */
	public int getClickType() {
		return clickType;
	}

	/**
	 * @param clickType
	 *            the clickType to set
	 */
	public void setClickType(int clickType) {
		this.clickType = clickType;
	}

	/**
	 * @return the clickParam
	 */
	public String getClickParam() {
		return clickParam;
	}

	/**
	 * @param clickParam
	 *            the clickParam to set
	 */
	public void setClickParam(String clickParam) {
		this.clickParam = clickParam;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TitleItemBeans) {
			if (((TitleItemBeans) o).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		// 把javanbean中的数据写到Parcel。注意顺序
		dest.writeString(contentId);
		dest.writeString(productId);
		dest.writeString(name);
		dest.writeInt(sortId);
		dest.writeString(clickParam);
		dest.writeInt(clickType);
		dest.writeInt(type);
	}

	public static final Creator<TitleItemBeans>

	CREATOR = new Creator<TitleItemBeans>()

	{

		@Override
		public TitleItemBeans createFromParcel(Parcel source) {
			TitleItemBeans titleItemBeans = new TitleItemBeans();
			titleItemBeans.setContentId(source.readString());
			titleItemBeans.setProductId(source.readString());
			titleItemBeans.setName(source.readString());
			titleItemBeans.setClickParam(source.readString());
			titleItemBeans.setClickType(source.readInt());
			titleItemBeans.setSortId(source.readInt());
			titleItemBeans.setType(source.readInt());
			return titleItemBeans;
		}

		@Override
		public TitleItemBeans[] newArray(int size) {
			return new TitleItemBeans[size];
		}
	};
}
