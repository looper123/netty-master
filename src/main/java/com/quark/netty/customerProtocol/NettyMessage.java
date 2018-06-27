package com.quark.netty.customerProtocol;


import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by ZhenpengLu on 2018/6/26.
 * netty协议实体类
 */
@Message
public final class NettyMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Header header;

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

//    @Override
//    public String toString() {
//        return "NettyMessage{" +
//                "header=" + header +
//                '}';
//    }
}
