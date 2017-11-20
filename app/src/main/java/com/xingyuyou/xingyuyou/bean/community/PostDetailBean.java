package com.xingyuyou.xingyuyou.bean.community;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostDetailBean implements Serializable {


    /**
     * id : 2917
     * posts_image : [{"posts_image":"2017-08-07/5987df423b993.jpg","posts_image_width":440,"posts_image_height":638,"posts_image_size":35973},{"posts_image":"2017-08-07/5987df423bb93.jpg","posts_image_width":440,"posts_image_height":295,"posts_image_size":32824},{"posts_image":"2017-08-07/5987df423bd67.jpg","posts_image_width":440,"posts_image_height":293,"posts_image_size":33467},{"posts_image":"2017-08-07/5987df423bf44.jpg","posts_image_width":440,"posts_image_height":295,"posts_image_size":32936}]
     * subject : 如何只把一个人p成高个儿
     * posts_forums : 0
     * thumbnail_image : [{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/4665987df423b993.jpg","thumbnail_width":440,"thumbnail_height":638},{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/7505987df423bb93.jpg","thumbnail_width":440,"thumbnail_height":295},{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/5525987df423bd67.jpg","thumbnail_width":440,"thumbnail_height":293},{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/1705987df423bf44.jpg","thumbnail_width":440,"thumbnail_height":295}]
     * message : 合影中如何只把一个人p成高个儿，第一次尝试，全部手机软件，一部手机完成，不够完美

     第三个恶搞p图，哈哈哈哈哈
     * dateline : 1502076738
     * posts_laud : 2
     * posts_collect : 0
     * post_type : 1
     * vod_id : null
     * nickname : 涵
     * head_image : http://xingyuyou.com/Public/app/user_image/59e07b2d02db0.jpg
     * uid : 570
     * medal : 1
     * rights_is : 1
     * vod_number : 1
     * posts_reset_image : [{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/5987df423b993.jpg","posts_image_width":440,"posts_image_height":638,"posts_image_size":35973},{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/5987df423bb93.jpg","posts_image_width":440,"posts_image_height":295,"posts_image_size":32824},{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/5987df423bd67.jpg","posts_image_width":440,"posts_image_height":293,"posts_image_size":33467},{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/5987df423bf44.jpg","posts_image_width":440,"posts_image_height":295,"posts_image_size":32936}]
     * laud_status : 0
     * collect_status : 0
     * relation : 0
     * operation_is : 0
     */

    private String id;
    private String subject;
    private String posts_forums;
    private String message;
    private String dateline;
    private String posts_laud;
    private String posts_collect;
    private String post_type;
    private String vod_id;
    private String nickname;
    private String head_image;
    private String uid;
    private String medal;
    private String rights_is;
    private String vod_number;
    private String laud_status;
    private String collect_status;
    private String user_rights_is;
    private int relation;
    private int operation_is;
    private List<PostsImageBean> posts_image;
    private List<ThumbnailImageBean> thumbnail_image;
    private List<PostsResetImageBean> posts_reset_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPosts_forums() {
        return posts_forums;
    }
    public String getUser_rights_is() {
        return user_rights_is;
    }

    public void setUser_rights_is(String user_rights_is) {
        this.user_rights_is = user_rights_is;
    }
    public void setPosts_forums(String posts_forums) {
        this.posts_forums = posts_forums;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getPosts_laud() {
        return posts_laud;
    }

    public void setPosts_laud(String posts_laud) {
        this.posts_laud = posts_laud;
    }

    public String getPosts_collect() {
        return posts_collect;
    }

    public void setPosts_collect(String posts_collect) {
        this.posts_collect = posts_collect;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getVod_id() {
        return vod_id;
    }

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

    public String getRights_is() {
        return rights_is;
    }

    public void setRights_is(String rights_is) {
        this.rights_is = rights_is;
    }

    public String getVod_number() {
        return vod_number;
    }

    public void setVod_number(String vod_number) {
        this.vod_number = vod_number;
    }

    public String getLaud_status() {
        return laud_status;
    }

    public void setLaud_status(String laud_status) {
        this.laud_status = laud_status;
    }

    public String getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(String collect_status) {
        this.collect_status = collect_status;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getOperation_is() {
        return operation_is;
    }

    public void setOperation_is(int operation_is) {
        this.operation_is = operation_is;
    }

    public List<PostsImageBean> getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(List<PostsImageBean> posts_image) {
        this.posts_image = posts_image;
    }

    public List<ThumbnailImageBean> getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(List<ThumbnailImageBean> thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public List<PostsResetImageBean> getPosts_reset_image() {
        return posts_reset_image;
    }

    public void setPosts_reset_image(List<PostsResetImageBean> posts_reset_image) {
        this.posts_reset_image = posts_reset_image;
    }

    public static class PostsImageBean {
        /**
         * posts_image : 2017-08-07/5987df423b993.jpg
         * posts_image_width : 440
         * posts_image_height : 638
         * posts_image_size : 35973
         */

        private String posts_image;
        private int posts_image_width;
        private int posts_image_height;
        private int posts_image_size;

        public String getPosts_image() {
            return posts_image;
        }

        public void setPosts_image(String posts_image) {
            this.posts_image = posts_image;
        }

        public int getPosts_image_width() {
            return posts_image_width;
        }

        public void setPosts_image_width(int posts_image_width) {
            this.posts_image_width = posts_image_width;
        }

        public int getPosts_image_height() {
            return posts_image_height;
        }

        public void setPosts_image_height(int posts_image_height) {
            this.posts_image_height = posts_image_height;
        }

        public int getPosts_image_size() {
            return posts_image_size;
        }

        public void setPosts_image_size(int posts_image_size) {
            this.posts_image_size = posts_image_size;
        }
    }

    public static class ThumbnailImageBean {
        /**
         * thumbnail_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/4665987df423b993.jpg
         * thumbnail_width : 440
         * thumbnail_height : 638
         */

        private String thumbnail_image;
        private int thumbnail_width;
        private int thumbnail_height;

        public String getThumbnail_image() {
            return thumbnail_image;
        }

        public void setThumbnail_image(String thumbnail_image) {
            this.thumbnail_image = thumbnail_image;
        }

        public int getThumbnail_width() {
            return thumbnail_width;
        }

        public void setThumbnail_width(int thumbnail_width) {
            this.thumbnail_width = thumbnail_width;
        }

        public int getThumbnail_height() {
            return thumbnail_height;
        }

        public void setThumbnail_height(int thumbnail_height) {
            this.thumbnail_height = thumbnail_height;
        }
    }

    public static class PostsResetImageBean {
        /**
         * posts_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-08-07/5987df423b993.jpg
         * posts_image_width : 440
         * posts_image_height : 638
         * posts_image_size : 35973
         */

        private String posts_image;
        private int posts_image_width;
        private int posts_image_height;
        private int posts_image_size;

        public String getPosts_image() {
            return posts_image;
        }

        public void setPosts_image(String posts_image) {
            this.posts_image = posts_image;
        }

        public int getPosts_image_width() {
            return posts_image_width;
        }

        public void setPosts_image_width(int posts_image_width) {
            this.posts_image_width = posts_image_width;
        }

        public int getPosts_image_height() {
            return posts_image_height;
        }

        public void setPosts_image_height(int posts_image_height) {
            this.posts_image_height = posts_image_height;
        }

        public int getPosts_image_size() {
            return posts_image_size;
        }

        public void setPosts_image_size(int posts_image_size) {
            this.posts_image_size = posts_image_size;
        }

        @Override
        public String toString() {
            return "PostsResetImageBean{" +
                    "posts_image='" + posts_image + '\'' +
                    ", posts_image_width=" + posts_image_width +
                    ", posts_image_height=" + posts_image_height +
                    ", posts_image_size=" + posts_image_size +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostDetailBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", posts_forums='" + posts_forums + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", posts_laud='" + posts_laud + '\'' +
                ", posts_collect='" + posts_collect + '\'' +
                ", post_type='" + post_type + '\'' +
                ", vod_id='" + vod_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", uid='" + uid + '\'' +
                ", medal='" + medal + '\'' +
                ", rights_is='" + rights_is + '\'' +
                ", vod_number='" + vod_number + '\'' +
                ", laud_status='" + laud_status + '\'' +
                ", collect_status='" + collect_status + '\'' +
                ", user_rights_is='" + user_rights_is + '\'' +
                ", relation=" + relation +
                ", operation_is=" + operation_is +
                ", posts_image=" + posts_image +
                ", thumbnail_image=" + thumbnail_image +
                ", posts_reset_image=" + posts_reset_image +
                '}';
    }
}
