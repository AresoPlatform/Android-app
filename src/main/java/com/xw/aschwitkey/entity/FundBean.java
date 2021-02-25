package com.xw.aschwitkey.entity;

import java.util.List;

public class FundBean {

    private List<Fund> data;

    private int recordsFiltered;

    private int recordsTotal;

    public List<Fund> getData() {
        return data;
    }

    public void setData(List<Fund> data) {
        this.data = data;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public static class Fund {
        private Integer id;

        private Integer publishAccount;

        private String fundName;

        private Integer fundExpect;

        private Integer fundShare;

        private Integer fundShareAmount;

        private double fundRate;

        private double fundInviteRate;

        private String fundAschAddress;

        private String fundGrantAddress;

        private Integer fundStatus;

        private Integer reviewStatus;

        private String reviewInfo;

        private Integer fundEndHeight;

        private String createtime;

        private String reviewEndTime;

        private String updatetime;

        private Integer sellAmount;

        private String buyStartTime; //认购开始时间

        private String endTime; //认购结束时间

        private String fundStartTime; //基金开始时间

        private String fundEndTime; //基金结束时间

        private String fundStartAccrual;//分红开始时间

        private String fundEndAccrual; //分红结束时间

        private Integer participants;

        private Integer yes;

        private Integer no;

        private boolean isShow;

        private Integer buyAmount;

        private TimeBean timeBean;

        public Integer getFundExpect() {
            return fundExpect;
        }

        public void setFundExpect(Integer fundExpect) {
            this.fundExpect = fundExpect;
        }

        public String getBuyStartTime() {
            return buyStartTime;
        }

        public void setBuyStartTime(String buyStartTime) {
            this.buyStartTime = buyStartTime;
        }

        public String getFundStartTime() {
            return fundStartTime;
        }

        public void setFundStartTime(String fundStartTime) {
            this.fundStartTime = fundStartTime;
        }

        public String getFundEndTime() {
            return fundEndTime;
        }

        public void setFundEndTime(String fundEndTime) {
            this.fundEndTime = fundEndTime;
        }

        public String getFundEndAccrual() {
            return fundEndAccrual;
        }

        public void setFundEndAccrual(String fundEndAccrual) {
            this.fundEndAccrual = fundEndAccrual;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPublishAccount() {
            return publishAccount;
        }

        public void setPublishAccount(Integer publishAccount) {
            this.publishAccount = publishAccount;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
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

        public double getFundRate() {
            return fundRate;
        }

        public void setFundRate(double fundRate) {
            this.fundRate = fundRate;
        }

        public double getFundInviteRate() {
            return fundInviteRate;
        }

        public void setFundInviteRate(double fundInviteRate) {
            this.fundInviteRate = fundInviteRate;
        }

        public String getFundAschAddress() {
            return fundAschAddress;
        }

        public void setFundAschAddress(String fundAschAddress) {
            this.fundAschAddress = fundAschAddress;
        }

        public Integer getFundEndHeight() {
            return fundEndHeight;
        }

        public void setFundEndHeight(Integer fundEndHeight) {
            this.fundEndHeight = fundEndHeight;
        }

        public String getFundStartAccrual() {
            return fundStartAccrual;
        }

        public void setFundStartAccrual(String fundStartAccrual) {
            this.fundStartAccrual = fundStartAccrual;
        }

        public Integer getFundStatus() {
            return fundStatus;
        }

        public void setFundStatus(Integer fundStatus) {
            this.fundStatus = fundStatus;
        }

        public Integer getReviewStatus() {
            return reviewStatus;
        }

        public void setReviewStatus(Integer reviewStatus) {
            this.reviewStatus = reviewStatus;
        }

        public Integer getSellAmount() {
            return sellAmount;
        }

        public void setSellAmount(Integer sellAmount) {
            this.sellAmount = sellAmount;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getFundGrantAddress() {
            return fundGrantAddress;
        }

        public void setFundGrantAddress(String fundGrantAddress) {
            this.fundGrantAddress = fundGrantAddress;
        }

        public String getReviewEndTime() {
            return reviewEndTime;
        }

        public void setReviewEndTime(String reviewEndTime) {
            this.reviewEndTime = reviewEndTime;
        }

        public String getReviewInfo() {
            return reviewInfo;
        }

        public void setReviewInfo(String reviewInfo) {
            this.reviewInfo = reviewInfo;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Integer getParticipants() {
            return participants;
        }

        public void setParticipants(Integer participants) {
            this.participants = participants;
        }

        public Integer getYes() {
            return yes;
        }

        public void setYes(Integer yes) {
            this.yes = yes;
        }

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public Integer getBuyAmount() {
            return buyAmount;
        }

        public void setBuyAmount(Integer buyAmount) {
            this.buyAmount = buyAmount;
        }

        public TimeBean getTimeBean() {
            return timeBean;
        }

        public void setTimeBean(TimeBean timeBean) {
            this.timeBean = timeBean;
        }
    }
}
