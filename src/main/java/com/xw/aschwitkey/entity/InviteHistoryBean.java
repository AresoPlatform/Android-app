package com.xw.aschwitkey.entity;

import java.util.List;

public class InviteHistoryBean {

    private List<HistoryBean> data;

    public List<HistoryBean> getData() {
        return data;
    }

    public void setData(List<HistoryBean> data) {
        this.data = data;
    }

    public static class HistoryBean {
        private String createtime;
        private String phone;
        private String nickName;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
