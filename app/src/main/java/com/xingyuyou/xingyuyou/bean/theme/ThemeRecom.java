package com.xingyuyou.xingyuyou.bean.theme;

import java.util.List;

/**
 * Created by 24002 on 2017/10/24.
 */

public class ThemeRecom {


    /**
     * status : 1
     * errorinfo : 获取推荐主题圈成功
     * data : [{"id":"72","class_name":"GALGAME","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877848e2.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877846a3.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc287784fc3.png","recommend_number":"1","recommend_status":0},{"id":"70","class_name":"一拳超人","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc4023458ec.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc4023426c5.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc402345f32.png","recommend_number":"2","recommend_status":0},{"id":"66","class_name":"决战平安京","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e8729159.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e8728ede.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e8729bf6.png","recommend_number":"1","recommend_status":0}]
     */

    private int status;
    private String errorinfo;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 72
         * class_name : GALGAME
         * class_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877848e2.png
         * class_head_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877846a3.png
         * class_virtual_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc287784fc3.png
         * recommend_number : 1
         * recommend_status : 0
         */

        private String id;
        private String class_name;
        private String class_image;
        private String class_head_image;
        private String class_virtual_image;
        private String recommend_number;
        private int recommend_status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getClass_image() {
            return class_image;
        }

        public void setClass_image(String class_image) {
            this.class_image = class_image;
        }

        public String getClass_head_image() {
            return class_head_image;
        }

        public void setClass_head_image(String class_head_image) {
            this.class_head_image = class_head_image;
        }

        public String getClass_virtual_image() {
            return class_virtual_image;
        }

        public void setClass_virtual_image(String class_virtual_image) {
            this.class_virtual_image = class_virtual_image;
        }

        public String getRecommend_number() {
            return recommend_number;
        }

        public void setRecommend_number(String recommend_number) {
            this.recommend_number = recommend_number;
        }

        public int getRecommend_status() {
            return recommend_status;
        }

        public void setRecommend_status(int recommend_status) {
            this.recommend_status = recommend_status;
        }
    }
}
