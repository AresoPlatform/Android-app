package com.xw.aschwitkey.entity;

import java.util.List;

public class ComRepBean {

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

        private String acceptUser;
        private int isRead;
        private Msg msg;
        private String sendTime;
        private String sendUser;
        private int type;

        public void setAcceptUser(String acceptUser) {
            this.acceptUser = acceptUser;
        }

        public String getAcceptUser() {
            return acceptUser;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setMsg(Msg msg) {
            this.msg = msg;
        }

        public Msg getMsg() {
            return msg;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public void setSendUser(String sendUser) {
            this.sendUser = sendUser;
        }

        public String getSendUser() {
            return sendUser;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public static class Msg {

        private int commentContentId;
        private String permlinkAuthor;
        private String headImage;
        private String commentContent;
        private String title;
        private int type;
        private String commentName;
        private String commentDate;
        private int commentId;
        private String json;
        private String comment;
        private String permlink;
        private String commentContentName;
        private String permlinkDate;

        public void setCommentContentId(int commentContentId) {
            this.commentContentId = commentContentId;
        }

        public int getCommentContentId() {
            return commentContentId;
        }

        public void setPermlinkAuthor(String permlinkAuthor) {
            this.permlinkAuthor = permlinkAuthor;
        }

        public String getPermlinkAuthor() {
            return permlinkAuthor;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setCommentName(String commentName) {
            this.commentName = commentName;
        }

        public String getCommentName() {
            return commentName;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return comment;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public String getPermlink() {
            return permlink;
        }

        public void setCommentContentName(String commentContentName) {
            this.commentContentName = commentContentName;
        }

        public String getCommentContentName() {
            return commentContentName;
        }

        public String getPermlinkDate() {
            return permlinkDate;
        }

        public void setPermlinkDate(String permlinkDate) {
            this.permlinkDate = permlinkDate;
        }
    }
}
