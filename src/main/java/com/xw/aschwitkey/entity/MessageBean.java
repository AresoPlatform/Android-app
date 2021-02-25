package com.xw.aschwitkey.entity;

public class MessageBean {
    private String content;
    private Integer count;
    private String headImage;
    private String sendTime;
    private String steemName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSteemName() {
        return steemName;
    }

    public void setSteemName(String steemName) {
        this.steemName = steemName;
    }
}
