package com.xingyuyou.xingyuyou.bean.community;

/**
 * Created by 24002 on 2017/5/12.
 */

public class TopViewRecommBean {

    /**
     * tid : 68
     * re_image : http://xingyuyou.com/Public/app/posts_image/1231231239b7a.jpg
     */

    private String tid;
    private String re_image;
    private String vod_id;

    public void setVod_id(String vod_id) {
        this.vod_id = vod_id;
    }

    public String getVod_id() {
        return vod_id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRe_image() {
        return re_image;
    }

    public void setRe_image(String re_image) {
        this.re_image = re_image;
    }
}
