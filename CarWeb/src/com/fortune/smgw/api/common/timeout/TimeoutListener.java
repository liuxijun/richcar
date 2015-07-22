package com.fortune.smgw.api.common.timeout;

import java.util.EventListener;

public abstract interface TimeoutListener extends EventListener
{
  public abstract void timeoutPerformed(TimeoutEvent paramTimeoutEvent);
}

