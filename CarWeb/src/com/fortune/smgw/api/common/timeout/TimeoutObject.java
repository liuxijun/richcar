package com.fortune.smgw.api.common.timeout;

import java.util.Calendar;

public class TimeoutObject
{
    private Object key;
    private long time;

    public Object getKey()
    {
        return this.key;
    }

    public long getTime() {
        return this.time;
    }

    public TimeoutObject(Object key) {
        this.key = key;
        this.time = Calendar.getInstance().getTimeInMillis();
    }
}