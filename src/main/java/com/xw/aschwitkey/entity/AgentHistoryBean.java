package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class AgentHistoryBean {
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

    public static class Result {
        private BigDecimal amount;
        private BigDecimal blackXas;
        private String delegator;
        private String delegatorTime;
        private String expireTime;
        private int id;
        private BigDecimal postCount;
        private String steemName;
        private BigDecimal voteCount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getBlackXas() {
            return blackXas;
        }

        public void setBlackXas(BigDecimal blackXas) {
            this.blackXas = blackXas;
        }

        public void setDelegator(String delegator) {
            this.delegator = delegator;
        }

        public String getDelegator() {
            return delegator;
        }

        public String getDelegatorTime() {
            return delegatorTime;
        }

        public void setDelegatorTime(String delegatorTime) {
            this.delegatorTime = delegatorTime;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public BigDecimal getPostCount() {
            return postCount;
        }

        public void setPostCount(BigDecimal postCount) {
            this.postCount = postCount;
        }

        public void setSteemName(String steemName) {
            this.steemName = steemName;
        }

        public String getSteemName() {
            return steemName;
        }

        public BigDecimal getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(BigDecimal voteCount) {
            this.voteCount = voteCount;
        }
    }

}
