package com.xw.aschwitkey.entity;

import java.util.List;

public class BountyTimeBean {
    private int code;
    private List<Result> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
        this.data = data;
    }

    public static class Result {
        private List<Week> weeks;
        private String year;

        public List<Week> getWeeks() {
            return weeks;
        }

        public void setWeeks(List<Week> weeks) {
            this.weeks = weeks;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public static class Week {
        private String end;
        private String start;
        private String week;

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }

}
