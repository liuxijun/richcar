package com.fortune.util;

import android.view.View;

/**
 * Created by xjliu on 2015/8/30.
 *
 */
public class ViewBean {
    private String title;
    private int rId;
    private int width;
    private int height;
    private int x;
    private int y;
    private int fontSize;
    private int fontColor;
    private View.OnClickListener onClickListener;
    public ViewBean() {
    }

    public ViewBean(String title, int rId, int width, int height, int x, int y) {
        this(title,rId,width,height,x,y,-1,-1,null);
    }
    public ViewBean(String title,int rId,int width,int height,int x,int y,int fontSize,int fontColor){
        this(title,rId,width,height,x,y,fontSize,fontColor,null);
    }
    public ViewBean(String title,int rId,int width,int height,int x,int y,int fontSize,int fontColor,View.OnClickListener onClickListener) {
        this.fontColor = fontColor;
        this.fontSize = fontSize;
        this.y = y;
        this.x = x;
        this.height = height;
        this.width = width;
        this.rId = rId;
        this.title = title;
        this.onClickListener = onClickListener;
    }

    public ViewBean(String title, int rId, int width, int height, int x, int y, int fontSize) {
        this(title,rId,width,height,x,y,fontSize,-1,null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
