package com.xw.aschwitkey.entity;

import java.util.List;

public class ChatBean {

    private String vxName;
    private List<Chat> history;

    public String getVxName() {
        return vxName;
    }

    public void setVxName(String vxName) {
        this.vxName = vxName;
    }

    public List<Chat> getHistory() {
        return history;
    }

    public void setHistory(List<Chat> history) {
        this.history = history;
    }

    public static class Chat {
        private String acceptHeadImage;
        private String acceptUser;
        private Integer isRead;
        private Object msg;
        private String sendTime;
        private String sendUser;
        private String sendHeadImage;
        private Integer type;

        public String getAcceptHeadImage() {
            return acceptHeadImage;
        }

        public void setAcceptHeadImage(String acceptHeadImage) {
            this.acceptHeadImage = acceptHeadImage;
        }

        public String getAcceptUser() {
            return acceptUser;
        }

        public void setAcceptUser(String acceptUser) {
            this.acceptUser = acceptUser;
        }

        public Integer getIsRead() {
            return isRead;
        }

        public void setIsRead(Integer isRead) {
            this.isRead = isRead;
        }

        public Object getMsg() {
            return msg;
        }

        public void setMsg(Object msg) {
            this.msg = msg;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getSendUser() {
            return sendUser;
        }

        public void setSendUser(String sendUser) {
            this.sendUser = sendUser;
        }

        public String getSendHeadImage() {
            return sendHeadImage;
        }

        public void setSendHeadImage(String sendHeadImage) {
            this.sendHeadImage = sendHeadImage;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
