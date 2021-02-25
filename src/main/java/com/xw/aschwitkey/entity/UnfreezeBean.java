package com.xw.aschwitkey.entity;

import java.math.BigDecimal;

public class UnfreezeBean {
    private String toAddress;
    private int type;
    private BigDecimal freezeNumber;
    private long unFreezeTime;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getFreezeNumber() {
        return freezeNumber;
    }

    public void setFreezeNumber(BigDecimal freezeNumber) {
        this.freezeNumber = freezeNumber;
    }

    public long getUnFreezeTime() {
        return unFreezeTime;
    }

    public void setUnFreezeTime(long unFreezeTime) {
        this.unFreezeTime = unFreezeTime;
    }
}
