package com.xingyuyou.xingyuyou.bean.theme;

import java.util.List;

/**
 * Created by 24002 on 2017/10/26.
 */

public class ThemeCircleItemData {

    /**
     * status : 1
     * errorinfo : 获取推荐主题圈成功
     * data : [{"id":"48","class_name":"神无月","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59e9a1c2f22fa.png","recommend_number":"0"},{"id":"46","class_name":"王者荣耀test","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59b0f031ea0d9.png","recommend_number":"0"},{"id":"55","class_name":"英雄杀","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59e9a5080a24f.png","recommend_number":"1"},{"id":"47","class_name":"尼尔机械纪元","class_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/forum_class/59e9a11d1cd68.png","recommend_number":"0"}]
     */

    private int status;
    private String errorinfo;
    private List<ThemeCircleListBean.DataBean.ThemeBean> data;

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

    public List<ThemeCircleListBean.DataBean.ThemeBean> getData() {
        return data;
    }

    public void setData(List<ThemeCircleListBean.DataBean.ThemeBean> data) {
        this.data = data;
    }



    @Override
    public String toString() {
        return "ThemeCircleItemData{" +
                "status=" + status +
                ", errorinfo='" + errorinfo + '\'' +
                ", data=" + data +
                '}';
    }
}
