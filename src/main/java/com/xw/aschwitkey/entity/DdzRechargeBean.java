package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class DdzRechargeBean {
    private List<Result> data;

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
        this.data = data;
    }

    public static class Result {
        private String createTime;
        private int isPay;
        private BigDecimal payTron;
        private BigDecimal getGold;
        private String updateTime;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getIsPay() {
            return isPay;
        }

        public void setIsPay(int isPay) {
            this.isPay = isPay;
        }

        public BigDecimal getPayTron() {
            return payTron;
        }

        public void setPayTron(BigDecimal payTron) {
            this.payTron = payTron;
        }

        public BigDecimal getGetGold() {
            return getGold;
        }

        public void setGetGold(BigDecimal getGold) {
            this.getGold = getGold;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
