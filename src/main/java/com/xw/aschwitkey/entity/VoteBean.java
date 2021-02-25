package com.xw.aschwitkey.entity;

import java.util.List;

public class VoteBean {

    private int code;
    private List<ResultBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultBean> getData() {
        return data;
    }

    public void setData(List<ResultBean> data) {
        this.data = data;
    }

    public static class ResultBean {

        private String acceptUser;
        private int isRead;
        private Msg msg;
        private String sendTime;
        private String sendUser;
        private int type;

        public void setAcceptUser(String acceptUser) {
            this.acceptUser = acceptUser;
        }

        public String getAcceptUser() {
            return acceptUser;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setMsg(Msg msg) {
            this.msg = msg;
        }

        public Msg getMsg() {
            return msg;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public void setSendUser(String sendUser) {
            this.sendUser = sendUser;
        }

        public String getSendUser() {
            return sendUser;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public static class Msg {

        private String voteName;
        private String voteHeadImage;
        private String voteDate;
        private String json;
        private String title;
        private String permlink;
        private String author;

        public void setVoteName(String voteName) {
            this.voteName = voteName;
        }

        public String getVoteName() {
            return voteName;
        }

        public void setVoteHeadImage(String voteHeadImage) {
            this.voteHeadImage = voteHeadImage;
        }

        public String getVoteHeadImage() {
            return voteHeadImage;
        }

        public String getVoteDate() {
            return voteDate;
        }

        public void setVoteDate(String voteDate) {
            this.voteDate = voteDate;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPermlink() {
            return permlink;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
