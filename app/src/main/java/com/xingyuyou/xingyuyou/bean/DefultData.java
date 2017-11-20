package com.xingyuyou.xingyuyou.bean;

/**
 * Created by 24002 on 2017/7/19.
 */

public class DefultData {

    /**
     * status : 1
     * errorinfo : 操作成功
     */

    private int status;
    private String errorinfo;

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

    @Override
    public String toString() {
        return "DefultData{" +
                "status=" + status +
                ", errorinfo='" + errorinfo + '\'' +
                '}';
    }
}
