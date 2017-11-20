package com.xingyuyou.xingyuyou.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class VideoInfo {

    private Bitmap bitmap;
    private String name;
    private String uri;
    private long size;
    private Bitmap uri_thumb;
    public VideoInfo(String name, String uri, long size, Bitmap uri_thumb, long duration) {
        super();
        this.name = name;
        this.uri = uri;
        this.size = size;
        this.uri_thumb = uri_thumb;
        this.duration = duration;
    }

    public Bitmap getUri_thumb() {
        return uri_thumb;
    }

    public void setUri_thumb(Bitmap uri_thumb) {
        this.uri_thumb = uri_thumb;
    }
    private long duration;//持续时间
    public VideoInfo(Bitmap bitmap, String name, String uri, long size, long duration) {
        super();
        this.bitmap = bitmap;
        this.name = name;
        this.uri = uri;
        this.size = size;
        this.duration = duration;
    }

    public VideoInfo(Bitmap bitmap, String name, long size, long duration) {
        super();
        this.bitmap = bitmap;
        this.name = name;
        this.size = size;
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public VideoInfo() {
        super();
    }
    public VideoInfo(Bitmap bitmap, String name, String uri) {
        super();
        this.bitmap = bitmap;
        this.name = name;
        this.uri = uri;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

}
