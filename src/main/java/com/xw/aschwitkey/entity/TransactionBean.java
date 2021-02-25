package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class TransactionBean {
    private List<History> transactions;

    public List<History> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<History> transactions) {
        this.transactions = transactions;
    }

    public static class History{
        private int type;
        private long timestamp;
        private String senderId;
        private String fee;
        private List<String> args;
        private String height;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public List<String> getArgs() {
            return args;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }
}
