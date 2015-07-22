package com.fortune.server.protocol;

/**
 * Created with IntelliJ IDEA.
 * User: xjliu
 * Date: 14-1-22
 * Time: ÉÏÎç10:10
 *
 */
public class StreamData{
    private int length;
    private byte[] data;
    public StreamData(){

    }
    public StreamData(byte[] data){
        this.data = data;
        length = data.length;
    }
    public StreamData(int length,byte[] data){
        this.length = length;
        this.data = data;
    }
    public int getLength(){
        return length;
    }
    public byte[] getData(){
        return data;
    }
}