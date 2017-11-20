package com.xingyuyou.xingyuyou.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2.
 */

public class PostClassListHeBean {


    public int status;
    public String errorinfo;
    public ForumThemeInfoBean forum_theme_info;
    public List<TopWellBean> top_well;
    public List<DataBean> data;

    public static class ForumThemeInfoBean {
        public String id;
        public String class_name;
        public String class_image;
        public String describe;
        public String class_head_image;
        public String class_virtual_image;
        public String type;
        public String dateline;
        public String posts_num;
        public String follow_relation;
        public int follow_count;

        public void setId(String id) {
            this.id = id;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public void setClass_image(String class_image) {
            this.class_image = class_image;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public void setClass_head_image(String class_head_image) {
            this.class_head_image = class_head_image;
        }

        public void setClass_virtual_image(String class_virtual_image) {
            this.class_virtual_image = class_virtual_image;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public void setPosts_num(String posts_num) {
            this.posts_num = posts_num;
        }

        public void setFollow_relation(String follow_relation) {
            this.follow_relation = follow_relation;
        }

        public void setFollow_count(int follow_count) {
            this.follow_count = follow_count;
        }

        public String getId() {

            return id;
        }

        public String getClass_name() {
            return class_name;
        }

        public String getClass_image() {
            return class_image;
        }

        public String getDescribe() {
            return describe;
        }

        public String getClass_head_image() {
            return class_head_image;
        }

        public String getClass_virtual_image() {
            return class_virtual_image;
        }

        public String getType() {
            return type;
        }

        public String getDateline() {
            return dateline;
        }

        public String getPosts_num() {
            return posts_num;
        }

        public String getFollow_relation() {
            return follow_relation;
        }

        public int getFollow_count() {
            return follow_count;
        }
    }

    public static class TopWellBean {
        public String id;
        public String subject;
        public String is_top;
        public String is_well;
    }

    public static class DataBean {
        public String id;
        public String subject;
        public String dateline;
        public String message;
        public String posts_laud;
        public String posts_forums;
        public String posts_collect;
        public String post_type;
        public Object vod_id;
        public String uid;
        public String nickname;
        public String head_image;
        public String medal;
        public int laud_status;
        public int collect_status;
        public String class_name;
        public List<ThumbnailImageBean> thumbnail_image;
        public List<PostsImageBean> posts_image;
        public List<LabelNameBean> label_name;

        public static class ThumbnailImageBean {
            public String thumbnail_image;
            public int thumbnail_width;
            public int thumbnail_height;
        }

        public static class PostsImageBean {
            public String posts_image;
            public int posts_image_width;
            public int posts_image_height;
            public int posts_image_size;
        }

        public static class LabelNameBean {
            public String label_name;
        }
    }
}
