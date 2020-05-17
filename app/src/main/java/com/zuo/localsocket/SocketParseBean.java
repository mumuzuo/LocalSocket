package com.zuo.localsocket;

/**
 * 解析socket服务传递的数据
 *
 * @author zuo
 * @date 2020/5/15 17:03
 */
public class SocketParseBean {

    private String info;
    private byte[] data;

    public SocketParseBean() {
    }

    public SocketParseBean(String info, byte[] data) {
        this.info = info;
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
