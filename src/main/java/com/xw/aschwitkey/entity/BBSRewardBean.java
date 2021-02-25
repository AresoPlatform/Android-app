package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class BBSRewardBean {

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

        private String rewardName;
        private String author;
        private String rewardHeadImage;
        private String json;
        private BigDecimal rewardAmount;
        private String rewardDate;
        private String title;
        private String permlink;

        public void setRewardName(String rewardName) {
            this.rewardName = rewardName;
        }

        public String getRewardName() {
            return rewardName;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }

        public void setRewardHeadImage(String rewardHeadImage) {
            this.rewardHeadImage = rewardHeadImage;
        }

        public String getRewardHeadImage() {
            return rewardHeadImage;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        public BigDecimal getRewardAmount() {
            return rewardAmount;
        }

        public void setRewardAmount(BigDecimal rewardAmount) {
            this.rewardAmount = rewardAmount;
        }

        public String getRewardDate() {
            return rewardDate;
        }

        public void setRewardDate(String rewardDate) {
            this.rewardDate = rewardDate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public String getPermlink() {
            return permlink;
        }
    }
}
