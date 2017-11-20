package com.xingyuyou.xingyuyou.bean;

import java.util.List;

/**
 * Created by 24002 on 2017/11/15.
 */

public class FenQuListBean {

    /**
     * status : 1
     * errorinfo : 社区或主题圈菜单
     * data : [{"id":"10","menu_name":"对方的"},{"id":"9","menu_name":"啊啊啊"},{"id":"5","menu_name":"撒打算"}]
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
         * id : 10
         * menu_name : 对方的
         */

        private String id;
        private String menu_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", menu_name='" + menu_name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FenQuListBean{" +
                "status=" + status +
                ", errorinfo='" + errorinfo + '\'' +
                ", data=" + data +
                '}';
    }
}
