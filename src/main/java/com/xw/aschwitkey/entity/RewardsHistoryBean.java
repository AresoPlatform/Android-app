package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class RewardsHistoryBean {
    private List<History> data;

    public List<History> getData() {
        return data;
    }

    public void setData(List<History> data) {
        this.data = data;
    }

    public static class History{

        private String phone;
        private String invTime;
        private Double amount;
        private boolean status;
        private String fundName;
        private int fundId;
        private String nickName;
        private BigDecimal fundTotalAmount;//奖励总额
        private BigDecimal currentlyAcquired;//时间获取的奖励总额
        private BigDecimal fundUpperAmount;//基金领取上限
        private Boolean isChaoshi;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getInvTime() {
            return invTime;
        }

        public void setInvTime(String invTime) {
            this.invTime = invTime;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public int getFundId() {
            return fundId;
        }

        public void setFundId(int fundId) {
            this.fundId = fundId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public BigDecimal getCurrentlyAcquired() {
            return currentlyAcquired;
        }

        public void setCurrentlyAcquired(BigDecimal currentlyAcquired) {
            this.currentlyAcquired = currentlyAcquired;
        }

        public BigDecimal getFundTotalAmount() {
            return fundTotalAmount;
        }

        public void setFundTotalAmount(BigDecimal fundTotalAmount) {
            this.fundTotalAmount = fundTotalAmount;
        }

        public BigDecimal getFundUpperAmount() {
            return fundUpperAmount;
        }

        public void setFundUpperAmount(BigDecimal fundUpperAmount) {
            this.fundUpperAmount = fundUpperAmount;
        }

        public Boolean getChaoshi() {
            return isChaoshi;
        }

        public void setChaoshi(Boolean chaoshi) {
            isChaoshi = chaoshi;
        }
    }
}
