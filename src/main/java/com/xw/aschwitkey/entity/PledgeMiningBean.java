package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class PledgeMiningBean {
    private int code;
    private List<History> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<History> getData() {
        return data;
    }

    public void setData(List<History> data) {
        this.data = data;
    }

    public static class History{
        private BigDecimal pledgeAmount;
        private String pledgeTime;
        private String startRewardTime;

        public BigDecimal getPledgeAmount() {
            return pledgeAmount;
        }

        public void setPledgeAmount(BigDecimal pledgeAmount) {
            this.pledgeAmount = pledgeAmount;
        }

        public String getPledgeTime() {
            return pledgeTime;
        }

        public void setPledgeTime(String pledgeTime) {
            this.pledgeTime = pledgeTime;
        }

        public String getStartRewardTime() {
            return startRewardTime;
        }

        public void setStartRewardTime(String startRewardTime) {
            this.startRewardTime = startRewardTime;
        }
    }
}
