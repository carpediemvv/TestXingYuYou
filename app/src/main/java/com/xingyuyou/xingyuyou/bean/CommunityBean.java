package com.xingyuyou.xingyuyou.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class CommunityBean {

    public int status;
    public String errorinfo;
    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String class_name;
        public String class_image;
        public int recommend_number;
        public int recommend_status;
        public String sortLetters;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", class_name='" + class_name + '\'' +
                    ", class_image='" + class_image + '\'' +
                    ", recommend_number=" + recommend_number +
                    ", recommend_status=" + recommend_status +
                    ", sortLetters='" + sortLetters + '\'' +
                    '}';
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public void setClass_image(String class_image) {
            this.class_image = class_image;
        }

        public void setRecommend_number(int recommend_number) {
            this.recommend_number = recommend_number;
        }

        public void setRecommend_status(int recommend_status) {
            this.recommend_status = recommend_status;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
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

        public int getRecommend_number() {
            return recommend_number;
        }

        public int getRecommend_status() {
            return recommend_status;
        }

        public String getSortLetters() {
            return sortLetters;
        }
    }
}
