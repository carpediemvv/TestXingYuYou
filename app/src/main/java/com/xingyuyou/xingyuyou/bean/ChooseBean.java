package com.xingyuyou.xingyuyou.bean;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ChooseBean {
    public String  id;
    public String  forum_name;

    public void setId(String id) {
        this.id = id;
    }

    public void setForum_name(String forum_name) {
        this.forum_name = forum_name;
    }

    public String getId() {

        return id;
    }

    public String getForum_name() {
        return forum_name;
    }

    @Override
    public String toString() {
        return "ChooseBean{" +
                "id='" + id + '\'' +
                ", forum_name='" + forum_name + '\'' +
                '}';
    }
}
