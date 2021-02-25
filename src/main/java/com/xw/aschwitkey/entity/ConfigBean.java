package com.xw.aschwitkey.entity;

public class ConfigBean {

    private int code;
    private DataBean data;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String Android_Download_Url;
        private String Android_Update_Content;
        private String Force_Open_Geographical_Position;
        private String Android_Version;
        private String Android_Update_Date;
        private String Tag_Mapping_Version;
        private String Android_Is_Force_Update;
        private String Android_Force_Update_Reason;

        public String getAndroid_Force_Update_Reason() {
            return Android_Force_Update_Reason;
        }

        public String getAndroid_Is_Force_Update() {
            return Android_Is_Force_Update;
        }

        public void setAndroid_Force_Update_Reason(String android_Force_Update_Reason) {
            Android_Force_Update_Reason = android_Force_Update_Reason;
        }

        public void setAndroid_Is_Force_Update(String android_Is_Force_Update) {
            Android_Is_Force_Update = android_Is_Force_Update;
        }

        public String getAndroid_Download_Url() {
            return Android_Download_Url;
        }

        public void setAndroid_Download_Url(String Android_Download_Url) {
            this.Android_Download_Url = Android_Download_Url;
        }

        public String getAndroid_Update_Content() {
            return Android_Update_Content;
        }

        public void setAndroid_Update_Content(String Android_Update_Content) {
            this.Android_Update_Content = Android_Update_Content;
        }

        public String getForce_Open_Geographical_Position() {
            return Force_Open_Geographical_Position;
        }

        public void setForce_Open_Geographical_Position(String Force_Open_Geographical_Position) {
            this.Force_Open_Geographical_Position = Force_Open_Geographical_Position;
        }

        public String getAndroid_Version() {
            return Android_Version;
        }

        public void setAndroid_Version(String Android_Version) {
            this.Android_Version = Android_Version;
        }

        public String getAndroid_Update_Date() {
            return Android_Update_Date;
        }

        public void setAndroid_Update_Date(String Android_Update_Date) {
            this.Android_Update_Date = Android_Update_Date;
        }

        public String getTag_Mapping_Version() {
            return Tag_Mapping_Version;
        }

        public void setTag_Mapping_Version(String Tag_Mapping_Version) {
            this.Tag_Mapping_Version = Tag_Mapping_Version;
        }
    }
}
