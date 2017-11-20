package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by 24002 on 2017/7/7.
 */

public class UserInfo {

    /**
     * status : 1
     * errorinfo : 获取社区首页列表成功
     * data : {"id":"105","nickname":"112","account":"18291910677","sex":"1","user_age":"1992-1-1","area":"四川-德阳","explain":"12111","hobby":"454","user_integral":"997","head_image":"http://xingyuyou.com/Public/app/user_image/5937590b83b43.jpg","address_info":"121","follow_num":"100","fans_num":"250","follow_status":"0"}
     */

    private int status;
    private String errorinfo;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 105
         * nickname : 112
         * account : 18291910677
         * sex : 1
         * user_age : 1992-1-1
         * area : 四川-德阳
         * explain : 12111
         * hobby : 454
         * user_integral : 997
         * head_image : http://xingyuyou.com/Public/app/user_image/5937590b83b43.jpg
         * address_info : 121
         * follow_num : 100
         * fans_num : 250
         * follow_status : 0
         */

        private String id;
        private String nickname;
        private String account;
        private String sex;
        private String user_age;
        private String area;
        private String explain;
        private String hobby;
        private String user_integral;
        private String head_image;
        private String address_info;
        private String follow_num;
        private String fans_num;
        private String follow_status;

        public String getBack_ground() {
            return back_ground;
        }

        public void setBack_ground(String back_ground) {
            this.back_ground = back_ground;
        }

        private String back_ground;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUser_age() {
            return user_age;
        }

        public void setUser_age(String user_age) {
            this.user_age = user_age;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getUser_integral() {
            return user_integral;
        }

        public void setUser_integral(String user_integral) {
            this.user_integral = user_integral;
        }

        public String getHead_image() {
            return head_image;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }

        public String getAddress_info() {
            return address_info;
        }

        public void setAddress_info(String address_info) {
            this.address_info = address_info;
        }

        public String getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(String follow_num) {
            this.follow_num = follow_num;
        }

        public String getFans_num() {
            return fans_num;
        }

        public void setFans_num(String fans_num) {
            this.fans_num = fans_num;
        }

        public String getFollow_status() {
            return follow_status;
        }

        public void setFollow_status(String follow_status) {
            this.follow_status = follow_status;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", account='" + account + '\'' +
                    ", sex='" + sex + '\'' +
                    ", user_age='" + user_age + '\'' +
                    ", area='" + area + '\'' +
                    ", explain='" + explain + '\'' +
                    ", hobby='" + hobby + '\'' +
                    ", user_integral='" + user_integral + '\'' +
                    ", head_image='" + head_image + '\'' +
                    ", address_info='" + address_info + '\'' +
                    ", follow_num='" + follow_num + '\'' +
                    ", fans_num='" + fans_num + '\'' +
                    ", follow_status='" + follow_status + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "status=" + status +
                ", errorinfo='" + errorinfo + '\'' +
                ", data=" + data +
                '}';
    }
}
