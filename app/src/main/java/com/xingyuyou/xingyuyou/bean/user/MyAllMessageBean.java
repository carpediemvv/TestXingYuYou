package com.xingyuyou.xingyuyou.bean.user;

import java.util.List;

/**
 * Created by 24002 on 2017/7/26.
 */

public class MyAllMessageBean {

    /**
     * status : 1
     * errorinfo : 获取列表成功
     * data : [{"id":"2","uid":"105","re_uid":"444","type":"2","pid":"0","title":"仙女关注你啦~撒花！","content":"0","dateline":"1500971000"},{"id":"5","uid":"105","re_uid":"444","type":"5","pid":"1399","title":"你的测试帖子被加精了！去看看......","content":"0","dateline":"1500970022"},{"id":"1","uid":"105","re_uid":"444","type":"1","pid":"1399","title":"仙女赞了你","content":"原贴：测试","dateline":"1500970000"},{"id":"3","uid":"105","re_uid":"444","type":"3","pid":"0","title":"仙女给你留了言！去看看......","content":"0","dateline":"1500961000"},{"id":"4","uid":"105","re_uid":"444","type":"4","pid":"1432","title":"仙女赞了你","content":"原评论：哈哈哈","dateline":"1500941207"}]
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
         * id : 2
         * uid : 105
         * re_uid : 444
         * type : 2
         * pid : 0
         * title : 仙女关注你啦~撒花！
         * content : 0
         * dateline : 1500971000
         */

        private String id;
        private String uid;
        private String re_uid;
        private String type;
        private String pid;
        private String title;
        private String content;
        private String dateline;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRe_uid() {
            return re_uid;
        }

        public void setRe_uid(String re_uid) {
            this.re_uid = re_uid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", uid='" + uid + '\'' +
                    ", re_uid='" + re_uid + '\'' +
                    ", type='" + type + '\'' +
                    ", pid='" + pid + '\'' +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", dateline='" + dateline + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MyAllMessageBean{" +
                "status=" + status +
                ", errorinfo='" + errorinfo + '\'' +
                ", data=" + data +
                '}';
    }
}
