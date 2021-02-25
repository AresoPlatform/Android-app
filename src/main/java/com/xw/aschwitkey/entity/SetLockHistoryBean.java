package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SetLockHistoryBean {
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
        private String aschAddress;
        private long startHeight;
        private long endHeight;
        private BigDecimal num;
        private BigDecimal txasNum;
        private String createTime;
        private int lockTimeType;

        public String getAschAddress() {
            return aschAddress;
        }

        public void setAschAddress(String aschAddress) {
            this.aschAddress = aschAddress;
        }

        public long getStartHeight() {
            return startHeight;
        }

        public void setStartHeight(long startHeight) {
            this.startHeight = startHeight;
        }

        public long getEndHeight() {
            return endHeight;
        }

        public void setEndHeight(long endHeight) {
            this.endHeight = endHeight;
        }

        public BigDecimal getNum() {
            return num;
        }

        public void setNum(BigDecimal num) {
            this.num = num;
        }

        public BigDecimal getTxasNum() {
            return txasNum;
        }

        public void setTxasNum(BigDecimal txasNum) {
            this.txasNum = txasNum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getLockTimeType() {
            return lockTimeType;
        }

        public void setLockTimeType(int lockTimeType) {
            this.lockTimeType = lockTimeType;
        }
    }
}
