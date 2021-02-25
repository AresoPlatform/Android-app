package com.xw.aschwitkey.entity;

import java.util.List;

public class FollowBean {

    private int code;

    private List<ResultBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResultBean> getData() {
        return data;
    }

    public void setData(List<ResultBean> data) {
        this.data = data;
    }

    public static class ResultBean {

        private String beFocusName;
        private String createTime;
        private String focusName;
        private int id;
        private String beFocusNameImage;
        private String focusNameImage;

        public String getBeFocusName() {
            return beFocusName;
        }

        public void setBeFocusName(String beFocusName) {
            this.beFocusName = beFocusName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFocusName() {
            return focusName;
        }

        public void setFocusName(String focusName) {
            this.focusName = focusName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBeFocusNameImage() {
            return beFocusNameImage;
        }

        public void setBeFocusNameImage(String beFocusNameImage) {
            this.beFocusNameImage = beFocusNameImage;
        }

        public String getFocusNameImage() {
            return focusNameImage;
        }

        public void setFocusNameImage(String focusNameImage) {
            this.focusNameImage = focusNameImage;
        }
    }
}
