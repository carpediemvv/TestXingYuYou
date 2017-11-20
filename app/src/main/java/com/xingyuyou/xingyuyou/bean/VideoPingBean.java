package com.xingyuyou.xingyuyou.bean;

/**
 * Created by Administrator on 2017/9/29.
 */

public class VideoPingBean {

    public String RequestId;
    public VideoMetaBean VideoMeta;
    public String PlayAuth;

    public static class VideoMetaBean {
        public String CoverURL;
        public String Status;
        public String VideoId;
        public double Duration;
        public String Title;
    }
}
