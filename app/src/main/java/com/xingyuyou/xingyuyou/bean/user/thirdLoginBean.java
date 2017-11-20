package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by 24002 on 2017/8/13.
 */

public class thirdLoginBean {

    /**
     * id : 42
     * account : 18501284627
     * qq_secret : 1
     * wx_secret : 1
     * wb_secret : 1
     */

    private String id;
    private String account;
    private String qq_secret;
    private String wx_secret;
    private String wb_secret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getQq_secret() {
        return qq_secret;
    }

    public void setQq_secret(String qq_secret) {
        this.qq_secret = qq_secret;
    }

    public String getWx_secret() {
        return wx_secret;
    }

    public void setWx_secret(String wx_secret) {
        this.wx_secret = wx_secret;
    }

    public String getWb_secret() {
        return wb_secret;
    }

    public void setWb_secret(String wb_secret) {
        this.wb_secret = wb_secret;
    }
}
