package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class WithdrawalHistoryBean {
    private int code;
    private List<Result> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
        this.data = data;
    }

    public static class Result{
        private int id;
        private BigDecimal withdrawAmount;
        private String time;
        private BigDecimal practicalAmount;
        private String accountingDate;
        private int withdrawStatus;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWithdrawStatus() {
            return withdrawStatus;
        }

        public void setWithdrawStatus(int withdrawStatus) {
            this.withdrawStatus = withdrawStatus;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAccountingDate() {
            return accountingDate;
        }

        public void setAccountingDate(String accountingDate) {
            this.accountingDate = accountingDate;
        }

        public BigDecimal getWithdrawAmount() {
            return withdrawAmount;
        }

        public void setWithdrawAmount(BigDecimal withdrawAmount) {
            this.withdrawAmount = withdrawAmount;
        }

        public BigDecimal getPracticalAmount() {
            return practicalAmount;
        }

        public void setPracticalAmount(BigDecimal practicalAmount) {
            this.practicalAmount = practicalAmount;
        }
    }
}
