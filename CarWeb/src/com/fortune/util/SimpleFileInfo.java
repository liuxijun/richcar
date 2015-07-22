package com.fortune.util;

import java.io.File;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 2011-2-28
 * Time: 12:47:18
 * 文件基础信息
 */
public class SimpleFileInfo  implements java.io.Serializable {
    private String name;
    private long size;
    private Date modifyDate;
    private boolean directory;
    private FileType type;
    private int width;
    private int height;
    private float length;
    private boolean selected;
    private String format;
    private String videoCodec;
    private String audioCodec;

    public SimpleFileInfo() {
    }

    public SimpleFileInfo(String name, long size, Date modifyDate, boolean directory, FileType type) {
        this.name = name;
        this.size = size;
        this.modifyDate = modifyDate;
        this.directory = directory;
        this.type = type;
    }
    public SimpleFileInfo(File file) {
        this.name = file.getAbsolutePath();
        this.size = file.length();
        this.modifyDate = new Date(file.lastModified());
        this.directory = file.isDirectory();
        this.type = FileUtils.getFileType(file.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public String toString(){
        return JsonUtils.getJsonString(this);
    }
}
