package com.fortune.smgw.api.common.timeout;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class TimeoutManager
{
    private List queue = Collections.synchronizedList(new LinkedList());

    private Timer timer = new Timer();

    private List listeners = Collections.synchronizedList(new LinkedList());
    private int seconds;

    public TimeoutManager(int seconds)
    {
        this.seconds = (seconds * 1000);
    }

    public void addTimeoutListener(TimeoutListener tol)
    {
        this.listeners.add(tol);
    }

    public void removeTimeoutListener(TimeoutListener tol)
    {
        this.listeners.remove(tol);
    }

    protected void notifyListener(TimeoutEvent event)
    {
        Iterator iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            TimeoutListener tol = (TimeoutListener)iterator.next();
            tol.timeoutPerformed(event);
        }
    }

    public void addTimeoutObject(TimeoutObject to) {
        this.queue.add(to);
    }

    public void clearTimeoutObject() {
        this.queue.clear();
    }

    public void start() {
        this.timer.schedule(new RealPlayer(null), 0L);
    }

    public void cancel() {
        this.timer.cancel();
    }
    private class RealPlayer extends TimerTask {
        private RealPlayer() {
        }
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000L);
                    long nowtime = Calendar.getInstance().getTimeInMillis();
                    Vector vector = new Vector();
                    int index = 0;
                    for (index = 0; index < TimeoutManager.this.queue.size(); index++) {
                        TimeoutObject to = (TimeoutObject)TimeoutManager.this.queue.get(index);
                        if (nowtime - to.getTime() < TimeoutManager.this.seconds) {
                            break;
                        }
                        vector.add(to.getKey());
                    }

                    if (index > 0) {
                        TimeoutManager.this.notifyListener(new TimeoutEvent(this, vector));
                        for (int j = 0; j < index; j++)
                            TimeoutManager.this.queue.remove(0);
                    }
                }
            }
            catch (Exception localException)
            {
            }
        }

        RealPlayer(RealPlayer arg2)
        {
            this();
        }
    }
}