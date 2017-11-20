package com.xingyuyou.xingyuyou.bean.theme;

import java.util.List;

/**
 * Created by 24002 on 2017/10/30.
 */

public class ThemeCircleDetailBean {


    /**
     * status : 1
     * errorinfo : 获取推荐主题圈成功
     * data : [{"id":"4","theme_type_name":"PC/主机","theme_type_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59e855031f49e.png","theme":[{"id":"72","class_name":"GALGAME","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877848e2.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877846a3.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc287784fc3.png","recommend_number":"0"},{"id":"55","class_name":"我的世界","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc28cbc28c9.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc28cbc268a.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc28cbc2caa.png","recommend_number":"2"}]},{"id":"2","theme_type_name":"手游","theme_type_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59e8550dedd96.png","theme":[{"id":"68","class_name":"Fate/Grand Order","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e069a6a2.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e069a41c.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e069bd75.png","recommend_number":"0"},{"id":"67","class_name":"奇迹暖暖","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e3bdd065.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e3bdcda0.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e3bde7f6.png","recommend_number":"1"},{"id":"66","class_name":"决战平安京","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e8729159.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e8728ede.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3e8729bf6.png","recommend_number":"1"},{"id":"48","class_name":"崩坏3","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3ebf191d4.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3ebf18f2b.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3ebf19987.png","recommend_number":"2"},{"id":"47","class_name":"开罗游戏","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3ee64bcbf.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3ee64bac7.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3ee64c2a8.png","recommend_number":"1"},{"id":"46","class_name":"王者荣耀","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3f17bb5d8.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3f17bb36e.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc3f17bc61f.png","recommend_number":"1"}]}]
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
         * id : 4
         * theme_type_name : PC/主机
         * theme_type_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59e855031f49e.png
         * theme : [{"id":"72","class_name":"GALGAME","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877848e2.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877846a3.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc287784fc3.png","recommend_number":"0"},{"id":"55","class_name":"我的世界","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc28cbc28c9.png","class_head_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc28cbc268a.png","class_virtual_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc28cbc2caa.png","recommend_number":"2"}]
         */

        private String id;
        private String theme_type_name;
        private String theme_type_image;
        private List<ThemeBean> theme;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTheme_type_name() {
            return theme_type_name;
        }

        public void setTheme_type_name(String theme_type_name) {
            this.theme_type_name = theme_type_name;
        }

        public String getTheme_type_image() {
            return theme_type_image;
        }

        public void setTheme_type_image(String theme_type_image) {
            this.theme_type_image = theme_type_image;
        }

        public List<ThemeBean> getTheme() {
            return theme;
        }

        public void setTheme(List<ThemeBean> theme) {
            this.theme = theme;
        }

        public static class ThemeBean {
            /**
             * id : 72
             * class_name : GALGAME
             * class_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877848e2.png
             * class_head_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc2877846a3.png
             * class_virtual_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59fc287784fc3.png
             * recommend_number : 0
             */

            private String id;
            private String class_name;
            private String class_image;
            private String class_head_image;
            private String class_virtual_image;
            private String recommend_number;

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
        }
    }
}
