package com.xw.aschwitkey.entity;

import java.util.List;

public class PurchasedBean {

    private List<Purchased> data;

    public List<Purchased> getData() {
        return data;
    }

    public void setData(List<Purchased> data) {
        this.data = data;
    }

    public static class Purchased {

        private double awardAmount;
        private Integer fundId;
        private String fundName;
        private Integer purchasedAmount;
        private Integer shenyuAmount;
        private double fundRate;
        private Integer fundShareAmount;
        private Integer fundExpect;

        public double getAwardAmount() {
            return awardAmount;
        }

        public void setAwardAmount(double awardAmount) {
            this.awardAmount = awardAmount;
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

        public Integer getPurchasedAmount() {
            return purchasedAmount;
        }

        public void setPurchasedAmount(Integer purchasedAmount) {
            this.purchasedAmount = purchasedAmount;
        }

        public Integer getShenyuAmount() {
            return shenyuAmount;
        }

        public void setShenyuAmount(Integer shenyuAmount) {
            this.shenyuAmount = shenyuAmount;
        }

        public double getFundRate() {
            return fundRate;
        }

        public void setFundRate(double fundRate) {
            this.fundRate = fundRate;
        }

        public Integer getFundShareAmount() {
            return fundShareAmount;
        }

        public void setFundShareAmount(Integer fundShareAmount) {
            this.fundShareAmount = fundShareAmount;
        }

        public Integer getFundExpect() {
            return fundExpect;
        }

        public void setFundExpect(Integer fundExpect) {
            this.fundExpect = fundExpect;
        }
    }
}
