package com.xingyuyou.xingyuyou.views;

import java.util.List;

public class ContactSortModel {


    public int status;
    public String errorinfo;
    public List<DataBean> data;

    public static class DataBean {

        public String nickname;
        public String sex;
        public String head_image;
        public String relation;
        public String sortLetters;
        public String tag;

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {

            return tag;
        }

        public String getEmchat_id() {
            return emchat_id;
        }

        public void setEmchat_id(String emchat_id) {
            this.emchat_id = emchat_id;
        }

        public String emchat_id;
        public String id;

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public String getSortLetters() {

            return sortLetters;
        }

        public void setId(String id) {
            this.id = id;
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

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getId() {

            return id;
        }

        public String getNickname() {
            return nickname;
        }

        public String getSex() {
            return sex;
        }

        public String getHead_image() {
            return head_image;
        }

        public String getRelation() {
            return relation;
        }
    }
}
