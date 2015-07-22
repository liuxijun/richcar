package com.fortune.smgw.api.common.timeout;

import java.util.EventObject;
import java.util.List;

public class TimeoutEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    private List keys;

    public List getKeys()
    {
        return this.keys;
    }

    public TimeoutEvent(Object source, List keys)
    {
        super(source);
        this.keys = keys;
    }
}