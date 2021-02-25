package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class RewardBean {

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
        private String createTime;
        private String fromName;
        private String fromAddress;
        private String fromImage;
        private Integer id;
        private String permlink;
        private BigDecimal rewardAmount;
        private String steemName;
        private String toAddress;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getFromImage() {
            return fromImage;
        }

        public void setFromImage(String fromImage) {
            this.fromImage = fromImage;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPermlink() {
            return permlink;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public BigDecimal getRewardAmount() {
            return rewardAmount;
        }

        public void setRewardAmount(BigDecimal rewardAmount) {
            this.rewardAmount = rewardAmount;
        }

        public String getSteemName() {
            return steemName;
        }

        public void setSteemName(String steemName) {
            this.steemName = steemName;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }
    }
}
