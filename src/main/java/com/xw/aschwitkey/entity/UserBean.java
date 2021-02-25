package com.xw.aschwitkey.entity;

public class UserBean {

    private boolean success;
    private AccountBean account;
    private LatestBlockBean latestBlock;
    private VersionBean version;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public LatestBlockBean getLatestBlock() {
        return latestBlock;
    }

    public void setLatestBlock(LatestBlockBean latestBlock) {
        this.latestBlock = latestBlock;
    }

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class AccountBean {

        private String address;
        private String balance;
        private String secondPublicKey;

        public String getSecondPublicKey() {
            return secondPublicKey;
        }

        public void setSecondPublicKey(String secondPublicKey) {
            this.secondPublicKey = secondPublicKey;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

    public static class LatestBlockBean {

        private int height;
        private long timestamp;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class VersionBean {

        private String version;
        private String build;
        private String net;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBuild() {
            return build;
        }

        public void setBuild(String build) {
            this.build = build;
        }

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }
    }
}