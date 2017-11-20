package com.xingyuyou.xingyuyou.bean.other;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class Reply_Bean implements Serializable{


    public int status;
    public String errorinfo;
    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String id;
        public String uid;
        public String re_uid;
        public String pid;
        public String content;
        public String dateline;
        public String nickname;
        public String medal;
        public String head_image;

        public void setId(String id) {
            this.id = id;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setRe_uid(String re_uid) {
            this.re_uid = re_uid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setMedal(String medal) {
            this.medal = medal;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }
    }
}