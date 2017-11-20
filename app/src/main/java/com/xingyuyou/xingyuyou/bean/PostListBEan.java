package com.xingyuyou.xingyuyou.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class PostListBEan {

    public int status;
    public String errorinfo;
    public int relation;
    public String posts_num;
    public List<?> top_well;
    public List<DataBean> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public void setPosts_num(String posts_num) {
        this.posts_num = posts_num;
    }

    public void setTop_well(List<?> top_well) {
        this.top_well = top_well;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getStatus() {

        return status;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public int getRelation() {
        return relation;
    }

    public String getPosts_num() {
        return posts_num;
    }

    public List<?> getTop_well() {
        return top_well;
    }

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        public String id;
        public String subject;
        public String dateline;
        public String fid;
        public String message;
        public String posts_laud;
        public String posts_forums;
        public String posts_collect;
        public String uid;
        public String nickname;
        public String head_image;
        public String medal;
        public int laud_status;
        public int collect_status;
        public String class_name;
        public List<ThumbnailImageBean> thumbnail_image;
        public List<?> posts_image;
        public List<LabelNameBean> label_name;

        public static class ThumbnailImageBean {
            public String thumbnail_image;
            public int thumbnail_width;
            public int thumbnail_height;
        }

        public static class LabelNameBean {
            public String label_name;
        }
    }
}
