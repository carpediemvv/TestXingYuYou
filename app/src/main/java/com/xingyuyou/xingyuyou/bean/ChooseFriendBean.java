package com.xingyuyou.xingyuyou.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ChooseFriendBean {

    public int status;
    public String errorinfo;
    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String class_name;
        public String class_image;
        public String sortLetters;
        public String tag;

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {

            return tag;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public String getSortLetters() {

            return sortLetters;
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

        public String getId() {
            return id;
        }

        public String getClass_name() {
            return class_name;
        }

        public String getClass_image() {
            return class_image;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", class_name='" + class_name + '\'' +
                    ", class_image='" + class_image + '\'' +
                    ", sortLetters='" + sortLetters + '\'' +
                    '}';
        }
    }


}
