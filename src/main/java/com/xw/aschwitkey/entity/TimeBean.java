package com.xw.aschwitkey.entity;

import android.os.SystemClock;

public class TimeBean {
    private long elapsedRealtime;
    public TimeBean(long remainTime) {
        elapsedRealtime = remainTime + SystemClock.elapsedRealtime()/1000;
    }


    public long getRainTime() {
        return elapsedRealtime - SystemClock.elapsedRealtime()/1000;
    }
}
