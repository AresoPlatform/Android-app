package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class FundDividendsBean {

    private List<FundDividends> data;

    public List<FundDividends> getData() {
        return data;
    }

    public void setData(List<FundDividends> data) {
        this.data = data;
    }

    public static class FundDividends {

        private Integer id;//购买的id

        private Integer fundId;//基金id

        private String fundName;

        private String fundExpect;

        private Integer fundStatus;//基金状态

        private Integer fundShare;

        private Integer fundShareAmount;//每份数量

        private BigDecimal fundRate;//利率

        private BigDecimal fundInviteRate;//邀请利率

        private String buyStartTime; //认购开始时间

        private String endTime; //认购结束时间

        private String fundStartTime; //基金开始时间

        private String fundEndTime; //基金结束时间

        private String fundStartAccrual;//分红开始时间

        private String fundEndAccrual; //分红结束时间

        private BigDecimal fundGrantAddressTotal;//分红地址总额

        private Integer sellAmount;//已认购数量

        private BigDecimal shenyu;//分红地址剩余

        private String fundGrantAddress;


        //用户
        private Integer isGet;//是否获取分红

        private Integer buyAmount;//购买的数量

        private String makeTime;//收取时间

        private Integer publishAccount;//发起人ID

        private double getAmount;

        public String getFundExpect() {
            return fundExpect;
        }

        public void setFundExpect(String fundExpect) {
            this.fundExpect = fundExpect;
        }

        public String getBuyStartTime() {
            return buyStartTime;
        }

        public void setBuyStartTime(String buyStartTime) {
            this.buyStartTime = buyStartTime;
        }

        public String getFundEndAccrual() {
            return fundEndAccrual;
        }

        public void setFundEndAccrual(String fundEndAccrual) {
            this.fundEndAccrual = fundEndAccrual;
        }

        public String getFundEndTime() {
            return fundEndTime;
        }

        public void setFundEndTime(String fundEndTime) {
            this.fundEndTime = fundEndTime;
        }

        public String getFundStartTime() {
            return fundStartTime;
        }

        public void setFundStartTime(String fundStartTime) {
            this.fundStartTime = fundStartTime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getFundId() {
            return fundId;
        }

        public void setFundId(Integer fundId) {
            this.fundId = fundId;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public Integer getFundStatus() {
            return fundStatus;
        }

        public void setFundStatus(Integer fundStatus) {
            this.fundStatus = fundStatus;
        }

        public Integer getFundShare() {
            return fundShare;
        }

        public void setFundShare(Integer fundShare) {
            this.fundShare = fundShare;
        }

        public Integer getFundShareAmount() {
            return fundShareAmount;
        }

        public void setFundShareAmount(Integer fundShareAmount) {
            this.fundShareAmount = fundShareAmount;
        }

        public BigDecimal getFundRate() {
            return fundRate;
        }

        public void setFundRate(BigDecimal fundRate) {
            this.fundRate = fundRate;
        }

        public BigDecimal getFundInviteRate() {
            return fundInviteRate;
        }

        public void setFundInviteRate(BigDecimal fundInviteRate) {
            this.fundInviteRate = fundInviteRate;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFundStartAccrual() {
            return fundStartAccrual;
        }

        public void setFundStartAccrual(String fundStartAccrual) {
            this.fundStartAccrual = fundStartAccrual;
        }

        public BigDecimal getFundGrantAddressTotal() {
            return fundGrantAddressTotal;
        }

        public void setFundGrantAddressTotal(BigDecimal fundGrantAddressTotal) {
            this.fundGrantAddressTotal = fundGrantAddressTotal;
        }

        public Integer getSellAmount() {
            return sellAmount;
        }

        public void setSellAmount(Integer sellAmount) {
            this.sellAmount = sellAmount;
        }

        public BigDecimal getShenyu() {
            return shenyu;
        }

        public void setShenyu(BigDecimal shenyu) {
            this.shenyu = shenyu;
        }

        public String getFundGrantAddress() {
            return fundGrantAddress;
        }

        public void setFundGrantAddress(String fundGrantAddress) {
            this.fundGrantAddress = fundGrantAddress;
        }

        public Integer getIsGet() {
            return isGet;
        }

        public void setIsGet(Integer isGet) {
            this.isGet = isGet;
        }

        public Integer getBuyAmount() {
            return buyAmount;
        }

        public void setBuyAmount(Integer buyAmount) {
            this.buyAmount = buyAmount;
        }

        public String getMakeTime() {
            return makeTime;
        }

        public void setMakeTime(String makeTime) {
            this.makeTime = makeTime;
        }

        public Integer getPublishAccount() {
            return publishAccount;
        }

        public void setPublishAccount(Integer publishAccount) {
            this.publishAccount = publishAccount;
        }

        public double getGetAmount() {
            return getAmount;
        }

        public void setGetAmount(double getAmount) {
            this.getAmount = getAmount;
        }
    }
}