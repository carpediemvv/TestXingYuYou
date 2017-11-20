package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by 24002 on 2017/8/31.
 */

public class BindPhoneBean {
    private String uid;
    private String login_type;
    private String qq_secret;
    private String wx_secret;
    private String wb_secret;
    private String mobile;
    private String vcode;
    private String password;

    public BindPhoneBean(String uid, String login_type, String qq_secret, String wx_secret, String wb_secret, String mobile, String vcode, String password) {
        this.uid = uid;
        this.login_type = login_type;
        this.qq_secret = qq_secret;
        this.wx_secret = wx_secret;
        this.wb_secret = wb_secret;
        this.mobile = mobile;
        this.vcode = vcode;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
