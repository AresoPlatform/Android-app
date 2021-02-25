package com.xw.aschwitkey.entity;

import java.util.List;

public class HistoryBean {

    private List<History> data;

    public List<History> getData() {
        return data;
    }

    public void setData(List<History> data) {
        this.data = data;
    }

    public static class History{
        private String amount;
        private String currency;
        private String dealTime;
        private Integer height;
        private String recipientId;
        private String senderId;
        private String tid;
        private Integer inOrout;
        private String message;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDealTime() {
            return dealTime;
        }

        public void setDealTime(String dealTime) {
            this.dealTime = dealTime;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(String recipientId) {
            this.recipientId = recipientId;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public Integer getInOrout() {
            return inOrout;
        }

        public void setInOrout(Integer inOrout) {
            this.inOrout = inOrout;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
