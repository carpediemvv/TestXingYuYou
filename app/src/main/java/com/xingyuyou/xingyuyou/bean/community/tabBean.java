package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by 24002 on 2017/11/13.
 */

public class tabBean {

    /**
     * status : 1
     * errorinfo : 获取推荐主题圈成功
     * data : [{"id":"1","menu_name":"可爱"}]
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
         * id : 1
         * menu_name : 可爱
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
        return "tabBean{" +
                "status=" + status +
                ", errorinfo='" + errorinfo + '\'' +
                ", data=" + data +
                '}';
    }
}
