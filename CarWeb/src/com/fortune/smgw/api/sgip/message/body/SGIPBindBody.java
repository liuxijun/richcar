package com.fortune.smgw.api.sgip.message.body;

import com.fortune.smgw.api.common.ByteHandler;
import com.fortune.smgw.api.common.TypeConvert;
import java.nio.ByteBuffer;

public class SGIPBindBody
{
    private int length = 41;
    private int loginType;
    private String loginName;
    private String loginPassword;
    private String reserve;

    public int getLoginType()
    {
        return this.loginType;
    }

    public void setLoginType(byte loginType) {
        this.loginType = loginType;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return this.loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(this.length);
        buffer.put((byte)this.loginType);
        buffer.put(TypeConvert.string2byte(this.loginName, 16));
        buffer.put(TypeConvert.string2byte(this.loginPassword, 16));
        buffer.put(TypeConvert.string2byte(this.reserve, 8));
        return buffer.array();
    }

    public static SGIPBindBody parse(ByteBuffer buffer) {
        SGIPBindBody body = new SGIPBindBody();
        body.loginType = ByteHandler.readUIntByOneByte(buffer);

        body.loginName = ByteHandler.readString(buffer, 16);

        body.loginPassword = ByteHandler.readString(buffer, 16);

        body.reserve = ByteHandler.readString(buffer, 8);

        return body;
    }

    public int getLength() {
        return this.length;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Login Type\t = " + this.loginType);
        sb.append("\nLogin Name\t = " + (this.loginName != null ? this.loginName : ""));
        sb.append("\nLogin Passowrd\t = " + (this.loginPassword != null ? this.loginPassword : ""));
        sb.append("\nReserve\t\t = " + (this.reserve != null ? this.reserve : ""));
        return sb.toString();
    }
}