package com.xw.aschwitkey.entity;

import java.util.List;

public class UpVoteBean {

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

        private int id;
        private String block_num;
        private String author;
        private String permlink;
        private String voter;
        private String voter_value;
        private String weight;
        private String post_create;
        private String timestamp;
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBlock_num() {
            return block_num;
        }

        public void setBlock_num(String block_num) {
            this.block_num = block_num;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPermlink() {
            return permlink;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public String getVoter() {
            return voter;
        }

        public void setVoter(String voter) {
            this.voter = voter;
        }

        public String getVoter_value() {
            return voter_value;
        }

        public void setVoter_value(String voter_value) {
            this.voter_value = voter_value;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getPost_create() {
            return post_create;
        }

        public void setPost_create(String post_create) {
            this.post_create = post_create;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
