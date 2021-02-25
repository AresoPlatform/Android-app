package com.xw.aschwitkey.entity;

import java.util.List;

public class Trc20HistoryBean {
    private List<History> data;

    public List<History> getData() {
        return data;
    }

    public void setData(List<History> data) {
        this.data = data;
    }

    public static class History{
        private String value;
        private String amount;
        private String from;
        private String to;
        private long block_timestamp;
        private String transaction_id;
        private String hash;
        private String type;
        private String contract_type;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public long getBlock_timestamp() {
            return block_timestamp;
        }

        public void setBlock_timestamp(long block_timestamp) {
            this.block_timestamp = block_timestamp;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContract_type() {
            return contract_type;
        }

        public void setContract_type(String contract_type) {
            this.contract_type = contract_type;
        }
    }
}
