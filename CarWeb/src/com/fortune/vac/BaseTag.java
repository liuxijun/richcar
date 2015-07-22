package com.fortune.vac;

/**
 * Created by IntelliJ IDEA.
 * User: xjliu
 * Date: 12-8-28
 * Time: 下午1:49
 * 处理TLV的基础类
 */

import com.fortune.util.AppConfigurator;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BaseTag {
    protected Logger logger = Logger.getLogger(getClass());
    protected long offset;
    protected long size;
    protected int type;
    protected long readed;
    protected byte[] buffers;
    protected int bufferOffset;
    protected int bufferPosNow;
    protected List<BaseTag> children = new ArrayList<BaseTag>(3);
    protected BaseTag parent;
    private String header="    ";

    public String getHeader(){
        return header;
    }
    public void setHeader(String header){
        this.header = header;
    }

    public static String intToType(int type) {
        StringBuilder st = new StringBuilder();
        st.append((char) ((type >> 24) & 0xff));
        st.append((char) ((type >> 16) & 0xff));
        st.append((char) ((type >> 8) & 0xff));
        st.append((char) (type & 0xff));
        return st.toString();
    }


    public static int typeToInt(String type) {
        return (type.charAt(0) << 24) + (type.charAt(1) << 16) + (type.charAt(2) << 8) + type.charAt(3);
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getReaded() {
        return readed;
    }

    public void setReaded(long readed) {
        this.readed = readed;
    }

    public List<BaseTag> getChildren() {
        return children;
    }

    public void setChildren(List<BaseTag> children) {
        this.children = children;
    }

    public BaseTag() {
    }

    public BaseTag(int type) {
        this.type = type;
    }

    public void init(long offset, long size,int type,long readed, byte[] buffers, int bufferPos) {
        this.offset = offset;
        this.size = size;
        this.readed = readed;
        this.buffers = buffers;
        this.bufferOffset = bufferPos;
        this.type = type;
        bufferPosNow = bufferPos + (int) readed;
        initSelf();
    }

    public void reset() {
        bufferPosNow = bufferOffset;
    }

    public void skip(int step) {
        bufferPosNow += step;
    }

    public long readBytes(int size) {
        long result = 0;
        if (bufferPosNow + size <= buffers.length) {
            int i = 0;
            while (i < size) {
                result <<= 8;
                result += (buffers[i + bufferPosNow] & 0xff);
                i++;
            }
            bufferPosNow += size;
        }
        return result;
    }

    public long read_64() {
        long highPart = read_32();

        return (highPart << 32) + read_32();
    }

    public long read_32() {
        return readBytes(4);
    }

    public int read_24() {
        return (int) readBytes(3);
    }

    public byte[] readByteArray(int maxLength){
        if (bufferPosNow + maxLength <= buffers.length) {
            bufferPosNow += maxLength;
        }
        byte[] result = new byte[maxLength];
        System.arraycopy(buffers,(bufferPosNow-maxLength),result,0,result.length);
        return result;
    }
    public String readString(int maxLength){
        int strLength = 0;
        if (bufferPosNow + size <= buffers.length) {
            while (strLength < maxLength) {
                if(buffers[strLength + bufferPosNow]==0){
                    break;
                }
                strLength ++;
            }
            bufferPosNow += maxLength;
        }
        if(strLength==0){
            return "";
        }
        byte[] result = new byte[strLength];
        try {
            System.arraycopy(buffers,(bufferPosNow-maxLength),result,0,result.length);
            return new String(result, AppConfigurator.getInstance().getConfig("vac.default.encoding","GBK"));
        } catch (Exception e) {
            logger.error("新建字符串时发生异常："+e.getMessage());
            return new String(result);
        }
    }
    public int read_16() {
        return (int) readBytes(2);
    }

    public byte read_8() {
        return buffers[bufferPosNow++];
    }

    public void initSelf() {
    }

    public void initAtoms(List<BaseTag> atoms) {
        long tagOffset = offset + readed;
        boolean hasMoreAtoms = true;
        boolean atom64;
        while (hasMoreAtoms) {
            int tagBufferOffset = bufferPosNow;
            long atomSize = read_32();
            int atomType = (int) read_32();
            int tagReaded = 8;
            if (atomSize <= 0) {
                logger.error("Error atom size!");
                break;
            }
            if (atomSize == 1) {
                //todo 深入处理64位偏移还没有做
                atom64 = true;
                atomSize = read_64();
                tagReaded += 8;
            } else {
                atom64 = false;
            }
            //logger.debug("get a atom of type "+atomType+"("+intToType(atomType)+"),size="+atomSize);
            boolean hasDealed = false;
            for (BaseTag atom : atoms) {
                if (atom.type == atomType) {
                    //atom.setWirteBox64(atom64);
                    atom.init(tagOffset, atomSize, (atomType), tagReaded, buffers, (bufferPosNow - tagReaded));
                    atoms.remove(atom);
                    hasDealed = true;
                    break;
                }
            }
            if (!hasDealed) {
            }
            tagOffset += atomSize;
            bufferPosNow = (int) (tagBufferOffset + atomSize);
            hasMoreAtoms =   (buffers.length>tagBufferOffset)&&(bufferPosNow-bufferOffset<size);
        }
    }

    public static int writeBytes(byte[] dataBuffer, long value, int size, int startPos) {
        if (startPos + size <= dataBuffer.length) {
            int i = size;
            while (i > 0) {
                i--;
                dataBuffer[i + startPos] = (byte) (value & 0xff);
                value >>= 8;
            }
        } else {
            return -1;
        }
        return startPos + size;
    }

    public static int writeByteArray(byte[] dataBuffer,byte[] data,int startPos,int dataLength){
        int endPos = startPos+dataLength;
        if(endPos>dataBuffer.length){
            dataLength = dataBuffer.length-startPos;
        }
        if(dataLength>data.length){
            dataLength = data.length;
        }
        System.arraycopy(data,0,dataBuffer,startPos,dataLength);
        return startPos+dataLength;
    }

    public static int writeString(byte[] dataBuffer, String value, int startPos) {
        if (value == null || "".equals(value)) {
            return startPos;
        }
        byte[] valueBytes = value.getBytes();
        if (startPos + valueBytes.length > dataBuffer.length) {
            return -1;
        }
        System.arraycopy(valueBytes, 0, dataBuffer, startPos, valueBytes.length);
        return startPos + valueBytes.length;
    }

    public static int write_64(byte[] dataBuffer, long value, int startPos) {
        return writeBytes(dataBuffer, value, 8, startPos);
    }

    public static int write_32(byte[] dataBuffer, long value, int startPos) {
        return writeBytes(dataBuffer, value, 4, startPos);
    }

    public static int write_24(byte[] dataBuffer, int value, int startPos) {
        return writeBytes(dataBuffer, value, 3, startPos);
    }

    public static int write_16(byte[] dataBuffer, int value, int startPos) {
        return writeBytes(dataBuffer, value, 2, startPos);
    }

    public static int write_8(byte[] dataBuffer, int value, int startPos) {
        return writeBytes(dataBuffer, value, 1, startPos);
    }

    public int rebuildSelf(byte[] dataBuffer, int startPos) {
        //前面的8个字节，4个size，4个type，跳过，不复制。size最后设置，type已经设置过了
        //logger.debug(BaseTag.intToType(type) + " atom not need rebuild self.");
        try {
            System.arraycopy(buffers, bufferOffset + 8, dataBuffer, startPos, (int) size - 8);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(BaseTag.intToType(type)+" atom rebuilding error! buffers.length=" + buffers.length+
                    ",bufferOffset+8="+(bufferOffset+8)+",dataBuffer.length=" + dataBuffer.length+
                    ",startPos="+startPos+",size-8="+(size-8));
        }
        return startPos + (int) size - 8;
    }

    public int rebuild(byte[] dataBuffer, int startPos) {
        if (size == 0) { //如果这个tag没有正确初始化，返回！
            return startPos;
        }
        int dataBegin = startPos;
        //跳过size部分！先写type
        startPos += 4;
        startPos = write_32(dataBuffer, type, startPos);
        startPos = rebuildSelf(dataBuffer, startPos);
        buffers = dataBuffer;
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                startPos = children.get(i).rebuild(dataBuffer, startPos);
            }
        }
        size = (startPos - dataBegin);
        bufferOffset = dataBegin;
        bufferPosNow = bufferOffset;
        offset = bufferOffset;
        write_32(dataBuffer, size, dataBegin);
        return startPos;
    }

    public String toHtml() {
        if(size<=0){
            return "";
        }
        StringBuffer result = new StringBuffer();
        result.append("<table border='1' cellspacing='0' width='100%'><tr><td width='100' valign='middle'>");
/*
        result.append(header).append("type=").append(intToType(type)).append(",offset=")
                .append(offset).append(",size=").append(size).append("\r\n");
*/
        result.append(intToType(type)).append("@").append(offset).append("<br/>size=").append(size);
        boolean childHeaderAdded = false;
        for(Field field :getClass().getDeclaredFields()){
            String clsName = field.getType().getName();
            String propertyName = field.getName();
            if(clsName.indexOf("com.fortune.stream.fileformat.mp4.tag")==0){
                try {
                    Object property = BeanUtils.getProperty(this, propertyName);
                    if(property!=null){
                        BeanUtils.setProperty(property,"header",header+"  ");

                        if(!childHeaderAdded){
                            childHeaderAdded = true;
                            result.append("</td><td width='*'><table width='100%'>");
                        }
                        result.append("<tr><td>");
                        result.append(property.toString()).append("");
                        result.append("</td></tr>");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        for(BaseTag tag:this.children){
            if(tag.getSize()<=0){
                continue;
            }
            if(!childHeaderAdded){
                childHeaderAdded = true;
                result.append("</td><td width='*'><table width='100%'>");
            }
            result.append("<tr><td>");
            result.append(tag.toString());
            result.append("</td></tr>");
        }
        if(childHeaderAdded){
            result.append("</table>");
        }
        result.append("</td></tr></table>\r\n");
        return result.toString();
    }

/*
    public String toXml() {
        XStream xs = new XStream();
        xs.alias(this.getClass().getSimpleName().toLowerCase(), this.getClass());
        String xml = xs.toXML(this);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append(xml);
        return sb.toString();
    }
*/

    public String toJson() {
        JSONArray json = JSONArray.fromObject(this);
        return json.toString();
    }

    public byte[] getBuffers() {
        return buffers;
    }

    public void setBuffers(byte[] buffers) {
        this.buffers = buffers;
    }

    public BaseTag getParent() {
        return parent;
    }

    public void setParent(BaseTag parent) {
        this.parent = parent;
    }
    
}

