package com.quark.netty.handler.customerProtocol;

/**
 * Created by ZhenpengLu on 2018/6/27.
 * NettyMessage  header类型枚举
 */
public enum MessageType {

    LOGIN_REQ(Byte.parseByte("111")),

    LOGIN_RESP(Byte.parseByte("110")),

    HEARTBEAT_RESP(Byte.parseByte("100")),

    HEARTBEAT_REQ(Byte.parseByte("011"));

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }


}
