package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午2:25
 * 消息头
 */
public class MessageHeader extends BaseTag{
    private int totalLength;
    private int commandId;
    private long sequenceId;

    public MessageHeader() {

    }

    public MessageHeader(int totalLength, int commandId, long sequenceId) {
        this.totalLength = totalLength;
        this.commandId = commandId;
        this.sequenceId = sequenceId;
    }

    public MessageHeader(byte[] buffer) {
        super();
        setBuffers(buffer);
        totalLength =(int) read_32();
        commandId = (int)read_32();
        sequenceId = (int)read_32();
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String toString(){
        return getClass().getSimpleName()+"{\r\n\ttotalLength="+totalLength +
                "\r\n\tcommandId=" +Command.commandNames.get(commandId)+"[0x"+Integer.toHexString(commandId)+"]" +
                "\r\n\tsequenceId="+sequenceId+"\r\n}\r\n";
    }
}
