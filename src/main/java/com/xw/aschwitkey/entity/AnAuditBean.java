package com.xw.aschwitkey.entity;

import java.util.List;

public class AnAuditBean {
    private List<Notice> data;

    private int recordsFiltered;

    private int recordsTotal;

    public List<Notice> getData() {
        return data;
    }

    public void setData(List<Notice> data) {
        this.data = data;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public static class Notice {
        private Integer accountId;
        private String content;
        private String coverImg;
        private String createtime;
        private Integer id;
        private Integer noticeStatus;
        private String reviewEndTime;
        private String reviewInfo;
        private Integer reviewStatus;
        private String title;
        private Integer participants;
        private Integer yes;
        private Integer no;
        private boolean isShow;

        public Integer getAccountId() {
            return accountId;
        }

        public void setAccountId(Integer accountId) {
            this.accountId = accountId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getNoticeStatus() {
            return noticeStatus;
        }

        public void setNoticeStatus(Integer noticeStatus) {
            this.noticeStatus = noticeStatus;
        }

        public String getReviewEndTime() {
            return reviewEndTime;
        }

        public void setReviewEndTime(String reviewEndTime) {
            this.reviewEndTime = reviewEndTime;
        }

        public String getReviewInfo() {
            return reviewInfo;
        }

        public void setReviewInfo(String reviewInfo) {
            this.reviewInfo = reviewInfo;
        }

        public Integer getReviewStatus() {
            return reviewStatus;
        }

        public void setReviewStatus(Integer reviewStatus) {
            this.reviewStatus = reviewStatus;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getParticipants() {
            return participants;
        }

        public void setParticipants(Integer participants) {
            this.participants = participants;
        }

        public Integer getYes() {
            return yes;
        }

        public void setYes(Integer yes) {
            this.yes = yes;
        }

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }
    }
}
