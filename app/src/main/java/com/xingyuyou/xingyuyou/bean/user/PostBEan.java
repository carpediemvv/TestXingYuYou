package com.xingyuyou.xingyuyou.bean.user;

import java.util.List;

/**
 * Created by Administrator on 2017/8/20.
 */

public class PostBEan {
    public int status;
    public String errorinfo;
    public int count_pages;
    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String pid;
        public String uid;
        public String replies_content;
        public String dateline;
        public String tid;
        public String floor_num;
        public String forum_laud;
        public String nickname;
        public String medal;
        public String head_image;
        public String laud_count;
        public int laud_status;
        public List<ImgarrBean> imgarr;
        public List<ThumbnailImageBean> thumbnail_image;

        public static class ImgarrBean {
            public String posts_image;
            public int posts_image_width;
            public int posts_image_height;
            public int posts_image_size;
        }

        public static class ThumbnailImageBean {
            public String thumbnail_image;
            public int thumbnail_width;
            public int thumbnail_height;
        }
    }
}

