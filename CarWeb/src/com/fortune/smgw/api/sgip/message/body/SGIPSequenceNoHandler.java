package com.fortune.smgw.api.sgip.message.body;

public class SGIPSequenceNoHandler
{
    private int number = 0;

    private static SGIPSequenceNoHandler singleton_instance = null;

    public static synchronized SGIPSequenceNoHandler getInstance() {
        if (singleton_instance == null) {
            singleton_instance = new SGIPSequenceNoHandler();
        }
        return singleton_instance;
    }

    public synchronized int getNumeber() {
        if (this.number > 65535)
            this.number = 0;
        else {
            this.number += 1;
        }

        return this.number;
    }
}