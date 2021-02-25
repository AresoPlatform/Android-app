package com.xw.aschwitkey.entity;

import java.util.List;

public class MsgClassBean {

    private List<ResultBean> msgClass;

    public List<ResultBean> getMsgClass() {
        return msgClass;
    }

    public void setMsgClass(List<ResultBean> msgClass) {
        this.msgClass = msgClass;
    }

    public static class ResultBean {
        private Integer count;
        private Object object;
        private Integer type;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
