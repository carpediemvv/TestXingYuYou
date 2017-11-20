package com.xingyuyou.xingyuyou.bean.theme;

/**
 * Created by 24002 on 2017/11/17.
 */

public class IsTopWellBean {

    /**
     * status : 1
     * errorinfo : 数据获取成功
     * data : {"is_well":"0","is_top":"1"}
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
         * is_well : 0
         * is_top : 1
         */

        private String is_well;
        private String is_top;

        public String getIs_well() {
            return is_well;
        }

        public void setIs_well(String is_well) {
            this.is_well = is_well;
        }

        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
        }
    }
}
