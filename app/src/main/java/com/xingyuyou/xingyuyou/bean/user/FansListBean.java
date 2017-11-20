package com.xingyuyou.xingyuyou.bean.user;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class FansListBean {


    public int status;
    public String errorinfo;
    public List<DataBean> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        public String uid;
        public String re_uid;
        public String relation;
        public String nickname;
        public String sex;
        public String head_image;
        public String medal;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setRe_uid(String re_uid) {
            this.re_uid = re_uid;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }

        public void setMedal(String medal) {
            this.medal = medal;
        }
    }
}
