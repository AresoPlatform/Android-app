package com.xw.aschwitkey.entity;

import java.util.List;

public class CommentBean {

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
        private String author;
        private String comment;
        private int commentId;
        private List<CmBean> comments;
        private String createTime;
        private int id;
        private String parentAuthor;
        private String parentPermlink;
        private int status;
        private String authorImage;

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getComment() {
            return comment;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public int getCommentId() {
            return commentId;
        }

        public List<CmBean> getComments() {
            return comments;
        }

        public void setComments(List<CmBean> comments) {
            this.comments = comments;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setParentAuthor(String parentAuthor) {
            this.parentAuthor = parentAuthor;
        }

        public String getParentAuthor() {
            return parentAuthor;
        }

        public void setParentPermlink(String parentPermlink) {
            this.parentPermlink = parentPermlink;
        }

        public String getParentPermlink() {
            return parentPermlink;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public String getAuthorImage() {
            return authorImage;
        }

        public void setAuthorImage(String authorImage) {
            this.authorImage = authorImage;
        }
    }

    public static class CmBean {
        private String author;
        private String comment;
        private String createTime;
        private String huifuPer;
        private String authorImage;
        private Integer id;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getHuifuPer() {
            return huifuPer;
        }

        public void setHuifuPer(String huifuPer) {
            this.huifuPer = huifuPer;
        }

        public String getAuthorImage() {
            return authorImage;
        }

        public void setAuthorImage(String authorImage) {
            this.authorImage = authorImage;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
