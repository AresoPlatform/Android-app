package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class DdzWithdrawalBean {
    private List<Result> data;

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
        this.data = data;
    }

    public static class Result{
        private String createTime;
        private int isOk;
        private BigDecimal serviceAmount;
        private BigDecimal withdrawAsoAmount;
        private BigDecimal withdrawGoldAmount;
        private String updateTime;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getIsOk() {
            return isOk;
        }

        public void setIsOk(int isOk) {
            this.isOk = isOk;
        }

        public BigDecimal getServiceAmount() {
            return serviceAmount;
        }

        public void setServiceAmount(BigDecimal serviceAmount) {
            this.serviceAmount = serviceAmount;
        }

        public BigDecimal getWithdrawAsoAmount() {
            return withdrawAsoAmount;
        }

        public void setWithdrawAsoAmount(BigDecimal withdrawAsoAmount) {
            this.withdrawAsoAmount = withdrawAsoAmount;
        }

        public BigDecimal getWithdrawGoldAmount() {
            return withdrawGoldAmount;
        }

        public void setWithdrawGoldAmount(BigDecimal withdrawGoldAmount) {
            this.withdrawGoldAmount = withdrawGoldAmount;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
