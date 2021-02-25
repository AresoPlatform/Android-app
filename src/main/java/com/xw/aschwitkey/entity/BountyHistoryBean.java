package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class BountyHistoryBean {
    private List<Result> rewardInfo;

    private BigDecimal xasCount;

    public List<Result> getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(List<Result> rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public BigDecimal getXasCount() {
        return xasCount;
    }

    public void setXasCount(BigDecimal xasCount) {
        this.xasCount = xasCount;
    }

    public static class Result {
        private String createTime;
        private String fromAddress;
        private String fromImage;
        private String fromName;
        private int id;
        private String jsonData;
        private String permlink;
        private BigDecimal rewardAmount;
        private String steemName;
        private String title;
        private String toAddress;

        public String getCreateTime() {
            return createTime;
        }

        public void setRewardAmount(BigDecimal rewardAmount) {
            this.rewardAmount = rewardAmount;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromImage(String fromImage) {
            this.fromImage = fromImage;
        }

        public String getFromImage() {
            return fromImage;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getFromName() {
            return fromName;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setJsonData(String jsonData) {
            this.jsonData = jsonData;
        }

        public String getJsonData() {
            return jsonData;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public String getPermlink() {
            return permlink;
        }

        public BigDecimal getRewardAmount() {
            return rewardAmount;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setSteemName(String steemName) {
            this.steemName = steemName;
        }

        public String getSteemName() {
            return steemName;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getToAddress() {
            return toAddress;
        }

    }


}
