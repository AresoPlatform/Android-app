package com.xw.aschwitkey.entity;

import java.util.Map;

public class EventMessageBean {

    private int tag;
    private Map messsage;

    public EventMessageBean(int tag, Map messsage) {
        this.tag = tag;
        this.messsage = messsage;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Map getMesssage() {
        return messsage;
    }

    public void setMesssage(Map messsage) {
        this.messsage = messsage;
    }
}
